package com.user.music.transcript.web.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "retry_event")
@SuperBuilder
public class RetryEvent extends BaseEntity{
    private Long userId;
    private String message;
    private String topicName;
}
