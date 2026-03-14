package com.user.music.transcript.web.service.impl;

import com.user.music.transcript.web.Entity.UserData;
import com.user.music.transcript.web.Entity.UserMusicData;
import com.user.music.transcript.web.Request.AudioUploadRequest;
import com.user.music.transcript.web.dao.IGenericDao;
import com.user.transcription.constants.Constant;
import com.user.transcription.service.IStorageService;
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

    @Override
    @Transactional
    public String publishRawFile(Long userId) {
        Optional<UserData> userData = iGenericDao.findById(Map.of("userId", userId), UserData.class);
        if(userData.isEmpty()) throw new IllegalArgumentException("UserData not found");
        String audioFile = getAudioFileName(userId,Constant.RAW_AUDIO);
        return storageService.generatePresignedUrl(audioFile);
    }

    @Override
    public boolean upsertTranscribedFile(UserMusicData userMusicData) {
        iGenericDao.upsert(userMusicData, Map.of("userId", userMusicData.getUserId(), "audioUrl", userMusicData.getAudioUrl()), UserMusicData.class);
        String audioFile = getAudioFileName(userMusicData.getUserId(), Constant.TRANSCRIBED_AUDIO);
        return storageService.publishAudioTranscriptionResult(userMusicData.getTranscriptUrl(), audioFile);
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

    private String getAudioFileName(Long userId, String prefix) {
        return prefix + "/" + userId + "/" + UUID.randomUUID();
    }
}
