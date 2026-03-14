package com.user.music.transcript.web.kafka.eventProcessors;

import com.user.music.transcript.web.model.MsgProcessResult;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface EventMessageProcessor {
    MsgProcessResult register(String message);
}
