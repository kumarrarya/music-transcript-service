package com.user.music.transcript.web.Request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AudioUploadRequest{
    @JsonProperty("Key")
    private String key;
}
