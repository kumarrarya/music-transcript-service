package com.user.music.transcript.web.Entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "device_tokens")
public class DeviceToken {
    @Id
    private String id;
    private String userId;
    private String deviceToken;
}
