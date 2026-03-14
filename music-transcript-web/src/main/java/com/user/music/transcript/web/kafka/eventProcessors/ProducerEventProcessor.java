package com.user.music.transcript.web.kafka.eventProcessors;

import com.user.music.transcript.web.Entity.RetryEvent;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Component;

@Component
public interface ProducerEventProcessor {
    void sendResult(String msg);
}
