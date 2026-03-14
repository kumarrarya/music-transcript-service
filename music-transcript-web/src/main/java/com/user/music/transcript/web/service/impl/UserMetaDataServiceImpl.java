package com.user.music.transcript.web.service.impl;

import com.user.music.transcript.web.Entity.UserData;
import com.user.music.transcript.web.Entity.UserMusicData;
import com.user.music.transcript.web.Request.AudioTranscriptionRequest;
import com.user.music.transcript.web.dao.IGenericDao;
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
    public String uploadFile(Long userId) {
        Optional<UserData> userData = iGenericDao.findById(Map.of("userId", userId), UserData.class);
        if(userData.isEmpty()) throw new IllegalArgumentException("UserData not found");
        String audioFile = getAudioFileName(userId);
        return storageService.generatePresignedUrl(audioFile);
    }

    @Override
    @Transactional
    public boolean upsertRawAudioUpload(UserMusicData userMusicData) {
        iGenericDao.upsert(userMusicData, Map.of("userId", userMusicData.getUserId()), UserMusicData.class);
        producerUtil.buildAudioTranscriptionEvent(userMusicData);
        return true;
    }

    @Override
    public boolean upsertAudioTranscriptionResult(UserMusicData userMusicData) {
        return iGenericDao.upsert(userMusicData, Map.of("userId", userMusicData.getUserId()), UserMusicData.class);
    }

    @Override
    public Optional<UserMusicData> getUserMusicData(AudioTranscriptionRequest request) {
        return iGenericDao.findById(Map.of("userId", request.getUserId(), "audioUrl", request.getAudioFileUrl()), UserMusicData.class);
    }

    private String getAudioFileName(Long userId){
        return (userId + "/audio/" + UUID.randomUUID());
    }
}
