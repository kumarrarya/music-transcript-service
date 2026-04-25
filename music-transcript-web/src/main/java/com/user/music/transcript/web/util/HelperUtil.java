package com.user.music.transcript.web.util;

import com.user.music.transcript.web.Entity.UserMusicData;

public class HelperUtil {

    public static String encodedFileNameForRawAudio(UserMusicData userMusicData) {
        return String.format("raw-audio/%s/%s",
                userMusicData.getUserId(),
                userMusicData.getAudioUrl());
    }

    public static String encodedFileNameForTranScriptedAudio(UserMusicData userMusicData) {
        return String.format("transcribed-audio/%s/%s",
                userMusicData.getUserId(),
                userMusicData.getTranscriptUrl());
    }
}
