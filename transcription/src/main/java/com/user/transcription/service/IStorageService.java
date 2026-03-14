package com.user.music.transcript.web.service;

import org.springframework.stereotype.Service;

@Service
public interface IStorageService {
    public String generatePresignedUrl(String fileName);
}
