package com.user.music.transcript.web.kafka.listeners;

import com.user.music.transcript.web.enums.TopicNameEnum;
import com.user.music.transcript.web.config.TopicConfig;
import com.user.music.transcript.web.kafka.eventProcessors.ProducerEventProcessor;
import com.user.music.transcript.web.kafka.handler.producer.AudioTranscriptionResultProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Configuration
public class HandlerProducerProcessorMap {

    @Autowired
    TopicConfig topicConfig;

    @Autowired
    private ApplicationContext applicationContext;

    private Map<TopicNameEnum, String> getTopicMap() {
        Map<TopicNameEnum, String> topicNameEnumStringMap = new HashMap<>();
        topicNameEnumStringMap.put(TopicNameEnum.AUDIO_MUSIC_INFO_TOPIC, topicConfig.getAudioUploadEventTopic());
        topicNameEnumStringMap.put(TopicNameEnum.AUDIO_TRANSCRIPTION_RESULT_TOPIC, topicConfig.getAudioTranscriptResultTopic());
        return topicNameEnumStringMap;
    }

    @Bean
    public Map<String, List<ProducerEventProcessor>> producerProcessorMap() {
        Map<TopicNameEnum, String> topicMap = getTopicMap();

        Map<String, List<ProducerEventProcessor>> pp = new HashMap<>();
        pp.put(
                topicMap.get(TopicNameEnum.AUDIO_TRANSCRIPTION_RESULT_TOPIC),
                List.of(applicationContext.getBean(AudioTranscriptionResultProducer.class))
        );
        return pp;
    }
}
