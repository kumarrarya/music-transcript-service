package com.user.music.transcript.web.kafka.listeners;

import com.user.music.transcript.web.config.TopicConfig;
import com.user.music.transcript.web.enums.TopicNameEnum;
import com.user.music.transcript.web.kafka.eventProcessors.EventMessageProcessor;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Configuration
public class HandlerConsumerProcessorMap {

    @Autowired
    TopicConfig topicConfig;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public List<String> kafkaConsumerAudioMusicInfoTopics() {
        Map<TopicNameEnum, String> topicNameEnumStringMap = new HashMap<>();
        topicNameEnumStringMap.put(TopicNameEnum.AUDIO_MUSIC_INFO_TOPIC,
                topicConfig.getAudioUploadEventTopic());
        return new ArrayList<>(topicNameEnumStringMap.values());
    }

    @Bean
    public List<String> kafkaConsumerAudioTranscriptResultTopics() {
        Map<TopicNameEnum, String> topicNameEnumStringMap = new HashMap<>();
        topicNameEnumStringMap.put(TopicNameEnum.AUDIO_TRANSCRIPTION_RESULT_TOPIC,
                topicConfig.getAudioTranscriptResultTopic());
        return new ArrayList<>(topicNameEnumStringMap.values());
    }

    @Bean("consumerTopicMap")
    public Map<TopicNameEnum, String> getConsumerTopicMap() {
        Map<TopicNameEnum, String> topicNameEnumStringMap = new HashMap<>();
        topicNameEnumStringMap.put(TopicNameEnum.AUDIO_MUSIC_INFO_TOPIC, topicConfig.getAudioUploadEventTopic());
        topicNameEnumStringMap.put(TopicNameEnum.AUDIO_TRANSCRIPTION_RESULT_TOPIC, topicConfig.getAudioTranscriptResultTopic());
        return topicNameEnumStringMap;
    }


    @Bean
    public Map<String, List<EventMessageProcessor>> processorMap() {
        Map<TopicNameEnum, String> map = getConsumerTopicMap();

        Map<TopicNameEnum, List<EventMessageProcessor>> pp = new ConcurrentHashMap<>();
        pp.put(
                TopicNameEnum.AUDIO_MUSIC_INFO_TOPIC,
                Arrays.asList(getBean("audioUploadEventHandler"))
        );
        pp.put(TopicNameEnum.AUDIO_TRANSCRIPTION_RESULT_TOPIC,
                Arrays.asList(getBean("audioTranscriptionHandler")));

        return flatMap(pp, map);
    }

    private Map<String, List<EventMessageProcessor>> flatMap(Map<TopicNameEnum, List<EventMessageProcessor>> pp,
                                                             Map<TopicNameEnum, String> topicNameEnumStringMap) {
        Map<String, List<EventMessageProcessor>> processorMap = new HashMap<>();
        for(TopicNameEnum topicNameEnum : pp.keySet()) {
            List<EventMessageProcessor> eventMessageProcessors = pp.get(topicNameEnum);
            String topicName = topicNameEnumStringMap.get(topicNameEnum);
            if(StringUtils.isBlank(topicName)){
                ((ConfigurableApplicationContext) applicationContext).close();
                throw new IllegalStateException("topicName is null");
            }
            if(!CollectionUtils.isEmpty(processorMap.get(topicName))){
                ((ConfigurableApplicationContext) applicationContext).close();
                throw new BeanCreationException("same topic resued in processor map: " + topicName
                    + " already exists for handler: " + processorMap.get(topicName)
                    + " conflict with handler: " + eventMessageProcessors
                );
            }
            processorMap.put(topicName, eventMessageProcessors);
        }
        return processorMap;
    }

    private EventMessageProcessor getBean(String beanName) {
        return applicationContext.getBean(beanName, EventMessageProcessor.class);
    }
}
