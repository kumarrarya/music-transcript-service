package com.user.music.transcript.web.kafka.handler.consumer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.music.transcript.web.Entity.UserMusicData;
import com.user.music.transcript.web.Request.NotificationUploadRequest;
import com.user.music.transcript.web.enums.MsgResultEnum;
import com.user.music.transcript.web.enums.Status;
import com.user.music.transcript.web.kafka.eventProcessors.EventMessageProcessor;
import com.user.music.transcript.web.model.MsgProcessResult;
import com.user.music.transcript.web.service.INotificationService;
import com.user.music.transcript.web.service.IUserMusicDataService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class AudioTranscriptionCompletedEventHandler implements EventMessageProcessor {

    @Autowired
    private INotificationService notificationService;

    @Autowired
    private IUserMusicDataService userMusicDataService;

    @Override
    @SneakyThrows
    public MsgProcessResult register(String message) {
        NotificationUploadRequest request = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(message, NotificationUploadRequest.class);
        MsgProcessResult msgProcessResult = new MsgProcessResult();
        msgProcessResult.setMsgResultEnum(MsgResultEnum.SUCCESS);
        UserMusicData userMusicData = getUserMusicData(request);
        boolean isSuccess;
        try{
            Optional<UserMusicData> dbUserMusicData = userMusicDataService.getUserMusicDataWithTranscriptUrl(userMusicData.getUserId(), userMusicData.getTranscriptUrl());
            if(dbUserMusicData.isEmpty()) {
                log.info("audio file is not processed yet for userId or not exist: {}, transcriptUrl: {}", userMusicData.getUserId(), userMusicData.getTranscriptUrl());
                msgProcessResult.setMsgResultEnum(MsgResultEnum.DISCARD);
            }
            userMusicData.setAudioUrl(dbUserMusicData.get().getAudioUrl());
            isSuccess = userMusicDataService.upsertNotification(userMusicData);
        }catch (Exception e) {
            log.info("Exception while processing notification event, message: {}, error: {}", message, e.getMessage());
            return MsgProcessResult.discard();
        }
        if(!isSuccess) {
            msgProcessResult.setMsgResultEnum(MsgResultEnum.DISCARD);
        }
        return msgProcessResult;
    }

    private UserMusicData getUserMusicData(NotificationUploadRequest request){
        String[] parts = request.getKey().split("/");

        String userId = parts[2];
        String url = parts[3];

        return UserMusicData.builder()
                .userId(Long.parseLong(userId))
                .transcriptUrl(url)
                .status(Status.NOTIFIED)
                .updateTime(new Date())
                .build();
    }
}
