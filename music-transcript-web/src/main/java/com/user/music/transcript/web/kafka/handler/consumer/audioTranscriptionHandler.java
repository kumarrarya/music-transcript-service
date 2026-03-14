package com.user.music.transcript.web.kafka.handler.consumer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.music.transcript.web.Entity.UserMusicData;
import com.user.music.transcript.web.Request.AudioTranscriptionRequest;
import com.user.music.transcript.web.enums.Status;
import com.user.music.transcript.web.kafka.eventProcessors.EventMessageProcessor;
import com.user.music.transcript.web.model.MsgProcessResult;
import com.user.music.transcript.web.service.IUserMusicDataService;
import com.user.transcription.service.ITranscriptionService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class audioTranscriptionHandler implements EventMessageProcessor {

    @Autowired
    ITranscriptionService transcriptionService;

    @Autowired
    IUserMusicDataService userMusicDataService;

    @Override
    @SneakyThrows
    public MsgProcessResult register(String message) {
        AudioTranscriptionRequest request = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(message, AudioTranscriptionRequest.class);
        boolean isSuccess = false;
        try {
            Optional<UserMusicData> dbUserMusicData = userMusicDataService.getUserMusicData(request);
            if(dbUserMusicData.isEmpty()) {
                log.warn("UserMusicData not found for userId: {}, audioFileUrl: {}", request.getUserId(), request.getAudioFileUrl());
                return MsgProcessResult.discard();
            }
            UserMusicData userMusicData = getUserMusicData(dbUserMusicData.get());
            if(!ObjectUtils.isEmpty(userMusicData)) {
                isSuccess = userMusicDataService.upsertAudioTranscriptionResult(userMusicData);
            }
        }catch (Exception e) {
            log.error("Error processing audio transcription for userId: {}, error: {}", request.getUserId(), e.getMessage());
            return MsgProcessResult.discard();
        }
        if(isSuccess) return MsgProcessResult.success();
        return MsgProcessResult.discard();
    }

    private UserMusicData getUserMusicData(UserMusicData dbUserMusicData) {
        String fileName = encodedFileName(dbUserMusicData);
        String transcript = transcriptionService.transcribeAudio(fileName);
        if(ObjectUtils.isEmpty(transcript)) {
            log.warn("Transcription result is empty for userId: {}, audioFileUrl: {}", dbUserMusicData.getUserId(), dbUserMusicData.getAudioUrl());
            return null;
        }
        return UserMusicData.builder()
                .userId(dbUserMusicData.getUserId())
                .audioUrl(dbUserMusicData.getAudioUrl())
                .status(Status.PROCESSED)
                .transcriptUrl(transcript)
                .updateTime(new Date())
                .build();
    }

    private String encodedFileName(UserMusicData userMusicData) {
        return String.format("%s/audio/%s", userMusicData.getUserId(), userMusicData.getAudioUrl());
    }

}
