package com.user.music.transcript.web.util;

import com.user.music.transcript.web.Entity.UserMusicData;
import com.user.music.transcript.web.config.TopicConfig;
import com.user.music.transcript.web.kafka.handler.producer.KafkaEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProducerUtil {

    @Autowired
    private KafkaEventProducer kafkaEventProducer;

    @Autowired
    private TopicConfig topicConfig;

    public void buildAudioTranscriptionEvent(Long userId, String url) {
       String message = String.format("{\"userId\": %d, \"audioFileUrl\": \"%s\"}", userId, url);
       kafkaEventProducer.sendEvent(message, topicConfig.getAudioTranscriptResultTopic());
    }

    public void publishAudioTranscriptionResult(UserMusicData userMusicData) {
        String message = String.format(
                "{\"userId\": %d, \"audioFileUrl\": \"%s\", \"transcriptUrl\": \"%s\"}",
                userMusicData.getUserId(),
                userMusicData.getAudioUrl(),
                userMusicData.getTranscriptUrl()
        );
        kafkaEventProducer.sendEvent(message, topicConfig.getPublishTranscriptResultTopic());
    }
}
