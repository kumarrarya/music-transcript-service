package com.user.transcription.service.impl;

import com.user.transcription.constants.Constant;
import com.user.transcription.service.IStorageService;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import io.minio.messages.UserMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@Slf4j
public class StorageServiceImpl implements IStorageService {

    private MinioClient minioClient;

    public StorageServiceImpl() {
        this.minioClient = MinioClient.builder()
                .endpoint("https://localhost:9000")
                .credentials("manish", "StrongPassword123")
                .build();
    }

    @Override
    public String generatePresignedUrl(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(Constant.BUCKET)
                            .object(fileName)
                            .expiry(60 * 10) // 10 minutes
                            .build()
            );
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getRawInputStream(String fileName){
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(Constant.BUCKET)
                            .object(fileName)
                            .build()
            );
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean publishAudioTranscriptionResult(String transcribedFile, String fileName) {
        try {
            byte[] data = transcribedFile.getBytes();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(Constant.BUCKET)
                            .object(fileName)
                            .stream(new ByteArrayInputStream(data), data.length, -1)
                            .contentType("text/plain")
                            .build()
            );
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
