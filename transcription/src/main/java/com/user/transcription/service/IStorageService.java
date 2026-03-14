package com.user.transcription.service;

import io.minio.messages.UserMetadata;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public interface IStorageService {
    String generatePresignedUrl(String fileName);
    InputStream getRawInputStream(String fileName);
    boolean publishAudioTranscriptionResult(String transcribedFile,String fileName);
}
