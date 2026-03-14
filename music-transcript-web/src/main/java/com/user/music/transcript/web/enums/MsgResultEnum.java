package com.user.music.transcript.web.enums;

import lombok.Data;

public enum MsgResultEnum {

    SUCCESS("SUCCESS", "process message success"),
    RETRY("RETRY", "process message retry"),
    DISCARD("DISCARD", "process message discard"),
    FAILED("FAILED", "process message failed"),
    UNKNOWN("UNKNOWN", "process message unknown");


    private final String code;
    private final String description;

    MsgResultEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
