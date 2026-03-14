package com.user.music.transcript.web.kafka.handler.consumer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.music.transcript.web.Entity.UserMusicData;
import com.user.music.transcript.web.Request.AudioUploadRequest;
import com.user.music.transcript.web.kafka.eventProcessors.EventMessageProcessor;
import com.user.music.transcript.web.model.MsgProcessResult;
import com.user.music.transcript.web.service.IUserMusicDataService;
import com.user.music.transcript.web.util.ProducerUtil;
import com.user.transcription.service.ITranscriptionService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Slf4j
@Component
public class RawToTranScriptedAudioHandler implements EventMessageProcessor {

    @Autowired
    ITranscriptionService transcriptionService;

    @Autowired
    IUserMusicDataService userMusicDataService;

    @Autowired
    ProducerUtil producerUtil;

    @Override
    @SneakyThrows
    public MsgProcessResult register(String message) {
        AudioUploadRequest request = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(message, AudioUploadRequest.class);
        boolean isSuccess = false;
        try {
            Optional<UserMusicData> dbUserMusicData = userMusicDataService.getUserMusicData(request);
            if(dbUserMusicData.isEmpty()) {
                log.warn("UserMusicData not found for userId: {}, audioFileUrl: {}", request.getUserId(), request.getAudioFileUrl());
                return MsgProcessResult.discard();
            }
            String fileName = encodedFileName(dbUserMusicData.get());
            String transcript = transcriptionService.transcribeAudio(fileName);
            if(ObjectUtils.isEmpty(transcript)) {
                log.warn("Transcription result is empty for userId: {}, audioFileUrl: {}", dbUserMusicData.get().getUserId(), dbUserMusicData.get().getAudioUrl());
                return null;
            }
            UserMusicData userMusicData = UserMusicData.builder()
                            .userId(dbUserMusicData.get().getUserId())
                            .audioUrl(dbUserMusicData.get().getAudioUrl())
                            .transcriptUrl(transcript)
                            .build();
            producerUtil.publishAudioTranscriptionResult(userMusicData);
        }catch (Exception e) {
            log.error("Error processing audio transcription for userId: {}, error: {}", request.getUserId(), e.getMessage());
            return MsgProcessResult.discard();
        }
        if(isSuccess) return MsgProcessResult.success();
        return MsgProcessResult.discard();
    }

    private String encodedFileName(UserMusicData userMusicData) {
        return String.format("raw-audio/%s/%s",
                userMusicData.getUserId(),
                userMusicData.getAudioUrl());
    }

}
