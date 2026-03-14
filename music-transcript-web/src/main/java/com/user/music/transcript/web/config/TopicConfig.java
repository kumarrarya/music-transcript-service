package com.user.music.transcript.web.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Getter
@Configuration
@NoArgsConstructor
public class TopicConfig {

    @Value(value = KafkaConfigProperties.AUDIO_UPLOAD_EVENT_TOPIC)
    private String audioUploadEventTopic;

    @Value(value = KafkaConfigProperties.AUDIO_TRANSCRIPTION_RESULT_TOPIC)
    private String audioTranscriptResultTopic;

    @Value(value = KafkaConfigProperties.PUBLISH_TRANSCRIPTION_RESULT_TOPIC)
    private String publishTranscriptResultTopic;

}
