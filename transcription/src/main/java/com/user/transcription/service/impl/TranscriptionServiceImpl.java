package com.user.transcription.service.impl;

import com.user.transcription.service.IStorageService;
import com.user.transcription.service.ITranscriptionService;
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
    IStorageService storageService;

    @Value("${elevenlabs.api.key}")
    private String apiKey;

    private final String URL = "https://api.elevenlabs.io/v1/speech-to-text";

    @Override
    @SneakyThrows
    public String transcribeAudio(String audioFilePath) {

        try {
            InputStream stream = storageService.getInputStream(audioFilePath);

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

            Response response = client.newCall(request).execute();

            try {
                String transcript = response.body().string();

                log.info("transcript : {}", transcript);

                return transcript;

            } catch (Exception e) {
                log.info("Error : {}", e.getMessage());
            }

        }catch (Exception e){
            log.info("Error : {}",e.getMessage());
        }
        return null;
    }
}
