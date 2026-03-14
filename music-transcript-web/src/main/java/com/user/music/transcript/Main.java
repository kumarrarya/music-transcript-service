package com.user.music.transcript;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {"com.user.music.transcript.web",
                "com.user.transcription"
        }
)
public class Main {
    public static void main(String[] args) {
      SpringApplication.run(Main.class, args);
    }
}