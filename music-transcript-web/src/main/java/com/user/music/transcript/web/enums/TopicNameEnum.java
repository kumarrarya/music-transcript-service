package com.user.music.transcript.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum TopicNameEnum {
    AUDIO_MUSIC_INFO_TOPIC("AUDIO_MUSIC_INFO_TOPIC","AUDIO_MUSIC_INFO_TOPIC", "AUDIO_MUSIC_INFO_TOPIC"),
    AUDIO_TRANSCRIPTION_RESULT_TOPIC("AUDIO_TRANSCRIPTION_RESULT_TOPIC","AUDIO_TRANSCRIPTION_RESULT_TOPIC","AUDIO_TRANSCRIPTION_RESULT_TOPIC"),
    PUBLISH_TRANSCRIPTION_RESULT_TOPIC("PUBLISH_TRANSCRIPTION_RESULT_TOPIC","PUBLISH_TRANSCRIPTION_RESULT_TOPIC","PUBLISH_TRANSCRIPTION_RESULT_TOPIC");

    private static final Map<String, TopicNameEnum> ENUM_MAP;

    static {
        ENUM_MAP = new HashMap<>();
        Arrays.stream(values()).forEach(t -> ENUM_MAP.put(t.name(), t));
    }

    private final String topicName;
    private final String topicDescName;
    private final String description;

    public static TopicNameEnum getByCode(String code){
        return ENUM_MAP.get(code);
    }
}
