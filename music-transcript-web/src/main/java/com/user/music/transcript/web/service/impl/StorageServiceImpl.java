package com.user.music.transcript.web.service.impl;

import com.user.music.transcript.web.constants.Constant;
import com.user.music.transcript.web.service.IStorageService;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.UserMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@Component
@Slf4j
public class StorageServiceImpl implements IStorageService {

    private MinioClient minioClient;

    private MinioClient publicMinioClient;

    @Value("${minio.endpoint}")
    private String minioEndpoint;
    @Value("${minio.access.key}")
    private String minioAccessKey;
    @Value("${minio.secret.key}")
    private String minioSecretKey;
    @Value("${minio.bucket.name}")
    private String minioBucket;

    @PostConstruct
    public void init() {
        try {
            log.debug("MinIO endpoint: " + minioEndpoint); // debug
            this.minioClient = MinioClient.builder()
                    .endpoint(minioEndpoint)
                    .credentials(minioAccessKey, minioSecretKey)
                    .build();
            String bucketName = minioBucket;
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.debug("Bucket created: " + bucketName);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generatePresignedUrlForAudioUpload(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(minioBucket)
                            .object(fileName)
                            .expiry(60 * 10)
                            .extraHeaders(Map.of(
                                    "Content-Type", "audio/mpeg"
                            ))// 10 minutes
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
                            .bucket(minioBucket)
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
                            .bucket(minioBucket)
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

    @Override
    public String generatePresignedUrlForAudioTranscriptionGet(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioBucket)
                            .object(fileName)
                            .expiry(60 * 60) // 1 hour
                            .build()
            );
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
