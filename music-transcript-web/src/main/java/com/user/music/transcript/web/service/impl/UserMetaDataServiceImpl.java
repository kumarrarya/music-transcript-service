package com.user.music.transcript.web.service.impl;

import com.user.music.transcript.web.Entity.UserData;
import com.user.music.transcript.web.Entity.UserMusicData;
import com.user.music.transcript.web.Request.AudioUploadRequest;
import com.user.music.transcript.web.dao.IGenericDao;
import com.user.music.transcript.web.constants.Constant;
import com.user.music.transcript.web.service.INotificationService;
import com.user.music.transcript.web.service.IStorageService;
import com.user.music.transcript.web.service.IUserMusicDataService;
import com.user.music.transcript.web.util.ProducerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserMetaDataServiceImpl implements IUserMusicDataService {

    @Autowired
    IStorageService storageService;

    @Autowired
    IGenericDao iGenericDao;

    @Autowired
    ProducerUtil producerUtil;

    @Autowired
    INotificationService notificationService;

    @Override
    @Transactional
    public String publishRawFile(Long userId) {
        Optional<UserData> userData = iGenericDao.findById(Map.of("userId", userId), UserData.class);
        if(userData.isEmpty()) throw new IllegalArgumentException("UserData not found");
        String audioFile = Constant.RAW_AUDIO + "/" + userId + "/" + UUID.randomUUID();
        return storageService.generatePresignedUrlForAudioUpload(audioFile);
    }

    @Override
    @Transactional
    public boolean upsertTranscribedFile(UserMusicData userMusicData, String transcriptMusic) {
        UUID uuid = UUID.randomUUID();
        String audioFile = Constant.TRANSCRIBED_AUDIO + "/" + userMusicData.getUserId() + "/" + uuid;
        userMusicData.setTranscriptUrl(uuid.toString());
        iGenericDao.upsert(userMusicData, Map.of("userId", userMusicData.getUserId(), "audioUrl", userMusicData.getAudioUrl()), UserMusicData.class);
        return storageService.publishAudioTranscriptionResult(transcriptMusic, audioFile);
    }

    @Override
    public boolean upsertNotification(UserMusicData userMusicData) {
        iGenericDao.upsert(userMusicData, Map.of("userId", userMusicData.getUserId(), "transcriptUrl", userMusicData.getTranscriptUrl()), UserMusicData.class);
        return notificationService.sendNotification(userMusicData);
    }

    @Override
    @Transactional
    public boolean upsertRawAudioUpload(UserMusicData userMusicData) {
        iGenericDao.upsert(userMusicData, Map.of("userId", userMusicData.getUserId(), "audioUrl", userMusicData.getAudioUrl()), UserMusicData.class);
        producerUtil.buildAudioTranscriptionEvent(userMusicData.getUserId(), userMusicData.getAudioUrl());
        return true;
    }

    @Override
    public Optional<UserMusicData> getUserMusicData(AudioUploadRequest request) {
        return iGenericDao.findById(Map.of("userId", request.getUserId(), "audioUrl", request.getAudioFileUrl()), UserMusicData.class);
    }

    @Override
    public Optional<UserMusicData> getUserMusicDataWithTranscriptUrl(Long userId, String transcriptUrl) {
        return iGenericDao.findById(Map.of("userId", userId, "transcriptUrl", transcriptUrl), UserMusicData.class);
    }
}
