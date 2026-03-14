package com.user.music.transcript.web.controller;

import com.user.music.transcript.web.service.IUserMusicDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/api/v1")
@Slf4j
public class UploadFileController {

    @Autowired
    IUserMusicDataService userMetaDataService;

    @GetMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam Long userId){
        try {
            String presignedUrl = userMetaDataService.publishRawFile(userId);
            return ResponseEntity.ok(presignedUrl);
        }catch (Exception e){
            log.info("unable to upload file", e);
            return ResponseEntity.ok(e.getMessage());
        }
    }
}
