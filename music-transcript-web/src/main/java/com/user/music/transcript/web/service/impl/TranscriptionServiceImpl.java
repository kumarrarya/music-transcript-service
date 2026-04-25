package com.user.music.transcript.web.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.music.transcript.web.service.IStorageService;
import com.user.music.transcript.web.service.ITranscriptionService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Slf4j
public class TranscriptionServiceImpl implements ITranscriptionService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    IStorageService storageService;

    @Value("${elevenlabs.api.key}")
    private String apiKey;

    private final String URL = "https://api.elevenlabs.io/v1/speech-to-text";

    @Override
    @SneakyThrows
    public String transcribeAudio(String audioFilePath) {

        try {
            InputStream stream = storageService.getRawInputStream(audioFilePath);

            byte[] audioBytes = stream.readAllBytes();

            OkHttpClient client = new OkHttpClient();

            RequestBody fileBody = RequestBody.create(
                    audioBytes,
                    MediaType.parse("audio/mpeg")
            );

            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "music", fileBody)
                    .addFormDataPart("model_id", "scribe_v1")
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.elevenlabs.io/v1/speech-to-text")
                    .addHeader("xi-api-key", apiKey)
                    .post(requestBody)
                    .build();

            //Response response = client.newCall(request).execute();
           String mockText = "This is a mock transcript for testing purposes. The actual transcription logic is commented out to avoid making real API calls during development.";
            try {
                String transcript;
                ObjectMapper objectMapper = new ObjectMapper();
                //transcript = objectMapper.readTree(response.body().string()).get("text").asText();
                log.info("transcript : {}", mockText);
                return mockText;
            } catch (Exception e) {
                log.info("Error : {}", e.getMessage());
            }

        }catch (Exception e){
            log.info("Error : {}",e.getMessage());
        }
        return null;
    }
}
