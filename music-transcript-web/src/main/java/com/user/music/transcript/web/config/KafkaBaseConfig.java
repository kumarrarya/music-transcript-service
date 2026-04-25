package com.user.music.transcript.web.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration("consumerBaseConfig")
@Getter
public class KafkaBaseConfig {

    @Value(value = KafkaConfigProperties.AUDIO_UPLOAD_EVENT_MAX_POLL_RECORDS)
    private String kafkaAudioUploadEventMaxPollRecords;

    @Value(value = KafkaConfigProperties.AUDIO_UPLOAD_EVENT_CONCURRENCY)
    private int kafkaAudioUploadEventConcurrency;

    @Value(value = KafkaConfigProperties.KAFKA_CONCURRENCY_FEED_ORDERS)
    private int kafkaConcurrencyFeedOrders;

    @Value(value = KafkaConfigProperties.KAFKA_MAX_POLL_RECORDS_FEED_ORDERS)
    private String kafkaMaxPollRecordsFeedOrders;

    @Value(value = KafkaConfigProperties.KAFKA_BOOTSTRAP_SERVERS)
    private String kafkaBootstrapServers;

    @Value(value = KafkaConfigProperties.KAFKA_TOPIC_GROUP_FEED_DATA)
    private String kafkaTopicGroupFeedData;


}
