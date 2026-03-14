package com.user.transcription.service.impl;

import com.user.transcription.service.IStorageService;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import io.minio.messages.UserMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Slf4j
public class StorageServiceImpl implements IStorageService {

    private MinioClient minioClient;

    private final String bucketName = "audio-bucket";


    public StorageServiceImpl() {
        this.minioClient = MinioClient.builder()
                .endpoint("https://localhost:9000")
                .credentials("manish", "StrongPassword123")
                .build();
    }

    @Override
    public String generatePresignedUrl(String fileName){
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(60 * 10) // 10 minutes
                            .build()
            );
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getInputStream(String fileName){
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
