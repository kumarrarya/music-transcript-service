package com.user.music.transcript.web.kafka.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.music.transcript.web.config.KafkaConfigProperties;
import com.user.music.transcript.web.config.TopicConfig;
import com.user.music.transcript.web.templates.KafkaBaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.util.Assert;
import java.util.Collections;


import static com.user.music.transcript.web.config.KafkaConfigProperties.AUDIO_UPLOAD_EVENT_TOPIC;

@Slf4j
@Configuration
public class AudioUploadEventListener extends KafkaBaseTemplate {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TopicConfig topicConfig;

    @Override
    @KafkaListener(id = "kafkaAudioUploadEventFeedData",
            topics = AUDIO_UPLOAD_EVENT_TOPIC,
            groupId = KafkaConfigProperties.KAFKA_TOPIC_GROUP_FEED_DATA,
            containerFactory = "kafkaBatchListenerContainerFactory"
    )
    public void kafkaMessageListener(String message, Acknowledgment ack) {
        Assert.notEmpty(Collections.singleton(message), "message is empty");
        handleMessage(message, topicConfig.getAudioUploadEventTopic(), ack);
    }
}