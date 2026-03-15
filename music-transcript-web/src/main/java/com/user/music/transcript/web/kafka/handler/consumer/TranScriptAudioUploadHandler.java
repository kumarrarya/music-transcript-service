package com.user.music.transcript.web.kafka.handler.consumer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.music.transcript.web.Entity.UserMusicData;
import com.user.music.transcript.web.Request.AudioUploadRequest;
import com.user.music.transcript.web.enums.MsgResultEnum;
import com.user.music.transcript.web.enums.Status;
import com.user.music.transcript.web.kafka.eventProcessors.EventMessageProcessor;
import com.user.music.transcript.web.model.MsgProcessResult;
import com.user.music.transcript.web.service.IUserMusicDataService;
import com.user.music.transcript.web.service.IStorageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class TranScriptAudioUploadHandler implements EventMessageProcessor {

    @Autowired
    private IUserMusicDataService userMusicDataService;

    @Autowired
    private IStorageService storageService;

    @Override
    @SneakyThrows
    public MsgProcessResult register(String message) {
        AudioUploadRequest request = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(message, AudioUploadRequest.class);
        MsgProcessResult msgProcessResult = new MsgProcessResult();
        msgProcessResult.setMsgResultEnum(MsgResultEnum.SUCCESS);
        boolean isSuccess;
        try {
            Optional<UserMusicData> dbUserMusicData = userMusicDataService.getUserMusicData(request);
            if(dbUserMusicData.isEmpty()) {
                log.warn("UserMusicData not found for userId: {}, audioFileUrl: {}", request.getUserId(), request.getAudioFileUrl());
                return MsgProcessResult.discard();
            }
            UserMusicData userMusicData = UserMusicData.builder()
                    .userId(dbUserMusicData.get().getUserId())
                    .audioUrl(dbUserMusicData.get().getAudioUrl())
                    .status(Status.PROCESSED)
                    .updateTime(new Date())
                    .build();
            isSuccess = userMusicDataService.upsertTranscribedFile(userMusicData);
        }catch (Exception e){
            log.info("Exception while processing transcript audio upload event, message: {}, error: {}", message, e.getMessage());
            return MsgProcessResult.discard();
        }
        if(!isSuccess) msgProcessResult.setMsgResultEnum(MsgResultEnum.DISCARD);
        return msgProcessResult;
    }
}

