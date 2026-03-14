package com.user.music.transcript.web.service;

import com.user.music.transcript.web.Entity.UserData;
import com.user.music.transcript.web.Entity.UserMusicData;
import com.user.music.transcript.web.Request.AudioTranscriptionRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IUserMusicDataService {
    String uploadFile(Long userId);
    boolean upsertRawAudioUpload(UserMusicData userMusicData);
    boolean upsertAudioTranscriptionResult(UserMusicData userMusicData);
    Optional<UserMusicData> getUserMusicData(AudioTranscriptionRequest request);
}
