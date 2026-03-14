package com.user.music.transcript.web.kafka.handler.producer;

import com.user.music.transcript.web.kafka.eventProcessors.ProducerEventProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class KafkaEventProducer {

    @Autowired
    @Qualifier("producerProcessorMap")
    private Map<String, List<ProducerEventProcessor>> producerProcessorMap;

    public <T> void sendEvent(String event, String topic){
        log.info("Producing event: {}", event);
        try {
            Assert.notNull(event, "producer message is null");
            List<ProducerEventProcessor> producerEventProcessors = producerProcessorMap.get(topic);
            for(ProducerEventProcessor producerEventProcessor : producerEventProcessors){
                producerEventProcessor.sendResult(event);
            }
        }catch (Exception e) {
            log.error("Error in sending message to topic : {}, error : {}", topic, e.getMessage());
        }
    }
}
