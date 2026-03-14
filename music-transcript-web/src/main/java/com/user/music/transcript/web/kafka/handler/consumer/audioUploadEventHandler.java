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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class audioUploadEventHandler implements EventMessageProcessor {

    @Autowired
    private IUserMusicDataService userMusicDataService;

    @Override
    @SneakyThrows
    public MsgProcessResult register(String message) {
        AudioUploadRequest request = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(message, AudioUploadRequest.class);
        MsgProcessResult msgProcessResult = new MsgProcessResult();
        msgProcessResult.setMsgResultEnum(MsgResultEnum.SUCCESS);
        boolean isSuccess;
        try {
            UserMusicData userMusicData = getUserMusicData(request);
            isSuccess = userMusicDataService.upsertRawAudioUpload(userMusicData);
        }catch (Exception e){
            return MsgProcessResult.discard();
        }
        if(!isSuccess) msgProcessResult.setMsgResultEnum(MsgResultEnum.DISCARD);
        return msgProcessResult;
    }

    private UserMusicData getUserMusicData(AudioUploadRequest request){
        String[] parts = request.getKey().split("/");

        String userId = parts[1];
        String url = parts[3];

        return UserMusicData.builder()
                .userId(Long.parseLong(userId))
                .audioUrl(url)
                .status(Status.UPLOADED)
                .updateTime(new Date())
                .build();
    }
}
