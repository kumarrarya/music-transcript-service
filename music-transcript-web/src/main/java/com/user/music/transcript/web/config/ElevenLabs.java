package com.user.music.transcript.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.vosk.Model;

import java.io.IOException;
import java.util.concurrent.Executor;

@Configuration
public class ElevenLabs {

    @Bean(name = "transcriptionThreadPool")
    public Executor transcriptionThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Adjust based on your CPU/RAM
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Vosk-");
        executor.initialize();
        return executor;
    }
}
