package com.user.music.transcript.web.service;

import com.user.music.transcript.web.Entity.UserMusicData;

public interface INotificationService {
    boolean sendNotification(UserMusicData userMusicData);
}
