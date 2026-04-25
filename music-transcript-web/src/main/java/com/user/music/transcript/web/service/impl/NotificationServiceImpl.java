package com.user.music.transcript.web.service.impl;


import com.google.cloud.storage.HttpMethod;
import com.user.music.transcript.web.Entity.DeviceToken;
import com.user.music.transcript.web.Entity.UserMusicData;
import com.user.music.transcript.web.dao.IGenericDao;
import com.user.music.transcript.web.service.INotificationService;
import com.user.music.transcript.web.service.IStorageService;
import com.user.music.transcript.web.util.HelperUtil;
import jakarta.mail.*;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    IGenericDao genericDao;

    @Autowired
    IStorageService storageService;

    @Override
    public boolean sendNotification(UserMusicData userMusicData) {
        List<DeviceToken> tokens = genericDao.findAll(Map.of("userId", userMusicData.getUserId()), DeviceToken.class);
        final String fromEmail = "manishkumar8882850061@gmail.com";
        final String password = "zsry kveh izdj aoxc";
        final String toEmail = "rdjmanishoo7@gmail.com";

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress("manishkumar8882850061@gmail.com"));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            String encodedFileName = HelperUtil.encodedFileNameForTranScriptedAudio(userMusicData);
            String downloadUrl = storageService.generatePresignedUrlForAudioTranscriptionGet(
                    encodedFileName
            );
            message.setSubject("Your Transcription is Ready 🎧");
            message.setContent("""
                    <html>
                    <body>
                        <p>Hi,</p>
                    
                        <p>Your audio transcription has been completed successfully.</p>
                    
                        <p>
                            <a href="%s" style="padding:10px 15px; background-color:#4CAF50; color:white; text-decoration:none; border-radius:5px;">
                                Download Transcript
                            </a>
                        </p>
                    
                        <p><b>Note:</b> This link may expire after some time for security reasons.</p>
                    
                        <p>Thanks,<br>Audio Transcription Service</p>
                    </body>
                    </html>
                    """.formatted(downloadUrl), "text/html");

            Transport.send(message);
            log.info("Email Sent Successfully!");
        } catch (MessagingException e) {
            log.info("There is no device token for userId: {}, notification cannot be sent", userMusicData.getUserId());
            e.printStackTrace();
        }
        return true;
    }
}
