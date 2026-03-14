package com.user.transcription.service;

import io.minio.messages.UserMetadata;

public interface ITranscriptionService {
    String transcribeAudio(String audioFilePath);
}
