package com.user.music.transcript.web.templates;

import com.user.music.transcript.web.enums.MsgResultEnum;
import com.user.music.transcript.web.kafka.eventProcessors.EventMessageProcessor;
import com.user.music.transcript.web.kafka.eventProcessors.ProducerEventProcessor;
import com.user.music.transcript.web.model.MsgProcessResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ExecutorService;

@Slf4j
@Configuration
public abstract class KafkaBaseTemplate {


    @Autowired
    @Qualifier("processorMap")
    private Map<String, List<EventMessageProcessor>> processorMap;

    @Autowired
    @Qualifier("messageConsumerExecutorService")
    private ExecutorService messageConsumerExecutorService;

    /**
     *  This is Kafka listener [Starting point to listen kafka batch]
     * @param message
     */
    public abstract void kafkaMessageListener(String message, Acknowledgment ack);

    protected boolean retryFiledMessages(Set<String> faileMessageSet){
        log.info("Message List in retryFaileMessages : {}", faileMessageSet);
        return true;
    }

    public void handleMessage(String message,String topic, Acknowledgment ack) {
        boolean success = processMessage(message, topic);
        if (success) {
            ack.acknowledge();
        }
    }

    /**
     * Use to process kafka message by passing message to respective handler
     * @param message
     * @return
     */
    protected boolean processMessage(String message, String topic) {
        try {
            Assert.notNull(message, "consumer message is null");

            List<EventMessageProcessor> messageProcessors = processorMap.get(topic);
            List<MsgResultEnum> messageProcessorResults = new ArrayList<>();
            for(EventMessageProcessor messageProcessor : messageProcessors){
                long now = System.currentTimeMillis();
                MsgProcessResult msgProcessResult =messageProcessor.register(message);
                messageProcessorResults.add(msgProcessResult.getMsgResultEnum());
            }
            boolean messageProcessFail = messageProcessorResults.contains(MsgResultEnum.RETRY);
            return !messageProcessFail;
        }catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}