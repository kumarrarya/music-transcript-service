package com.user.music.transcript.web.config;


public class KafkaConfigProperties {

    public static final String AUDIO_UPLOAD_EVENT_MAX_POLL_RECORDS = "${kafka.audio.upload.event.max.poll.records}";
    public static final String AUDIO_UPLOAD_EVENT_CONCURRENCY = "${kafka.audio.upload.event.concurrency}";
    public static final String AUDIO_UPLOAD_EVENT_TOPIC = "${kafka.audio.upload.event.topic}";
    public static final String KAFKA_TOPIC_GROUP_FEED_DATA = "${kafka.topic.group.feed.data}";
    public static final String KAFKA_CONCURRENCY_FEED_ORDERS = "${kafka.concurrency.feed.orders}";
    public static final String KAFKA_MAX_POLL_RECORDS_FEED_ORDERS = "${kafka.max.poll.records.feed.orders}";
    public static final String KAFKA_BOOTSTRAP_SERVERS = "${spring.kafka.consumer.bootstrap-servers}";
    public static final String AUDIO_TRANSCRIPTION_RESULT_TOPIC = "${kafka.audio.transcription.result.topic}";
    public static final String PUBLISH_TRANSCRIPTION_RESULT_TOPIC = "${kafka.publish.transcription.result.topic}";
}
