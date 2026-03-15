package com.user.music.transcript.web.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.user.music.transcript.web.Entity.DeviceToken;
import com.user.music.transcript.web.Entity.UserMusicData;
import com.user.music.transcript.web.dao.IGenericDao;
import com.user.music.transcript.web.service.INotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    IGenericDao genericDao;
    @Override
    public boolean sendNotification(UserMusicData userMusicData) {
        List<DeviceToken> tokens = genericDao.findAll(Map.of("userId", userMusicData.getUserId()), DeviceToken.class);

        for (DeviceToken token : tokens) {
            Message message = Message.builder()
                    .setToken(token.getDeviceToken())
                    .setNotification(Notification.builder()
                            .setTitle(("Audio Transcription Completed"))
                            .setBody("Your audio file has been transcribed successfully. You can view the transcript now!")
                            .build())
                    .build();
            try {
                FirebaseMessaging.getInstance().send(message);
                log.info("Notification sent");
                return true;
            } catch (Exception e) {
                log.error("Error sending notification: {}", e.getMessage());
            }
        }
        log.info("There is no device token for userId: {}, notification cannot be sent", userMusicData.getUserId());
        return true;
    }
}
