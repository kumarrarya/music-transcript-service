package com.user.music.transcript.web.kafka.handler.producer;

import com.user.music.transcript.web.config.TopicConfig;
import com.user.music.transcript.web.kafka.eventProcessors.ProducerEventProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class PublishTranscriptedAudioProducer implements ProducerEventProcessor {

    @Autowired
    private TopicConfig topicConfig;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendResult(String msg) {
        try {
            kafkaTemplate.send(
                    topicConfig.getPublishTranscriptResultTopic(),
                    msg
            );
            log.info("Produced transcription result -> {}", msg);
        } catch (Exception e) {
            throw new RuntimeException("Failed to produce transcription result", e);
        }
    }
}
