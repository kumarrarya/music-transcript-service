package com.user.music.transcript.web.service;

import com.user.music.transcript.web.Entity.UserMusicData;
import com.user.music.transcript.web.Request.AudioUploadRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IUserMusicDataService {
    String publishRawFile(Long userId);
    boolean upsertRawAudioUpload(UserMusicData userMusicData);
    Optional<UserMusicData> getUserMusicData(AudioUploadRequest request);
    Optional<UserMusicData> getUserMusicDataWithTranscriptUrl(Long userId, String transcriptUrl);
    boolean upsertTranscribedFile(UserMusicData userMusicData);
    boolean upsertNotification(UserMusicData userMusicData);
}
