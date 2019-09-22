package com.app.service.email_service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
public final class EmailServiceKM {

    private static final String EMAIL_ADDRESS = "dw.test.programming@gmail.com";
    private static final String EMAIL_PASSWORD = "programmingtest1";

    public static void sendAsHtml( String to, String title, String html ) {
        try {
            log.info("Sending email to " + to);
            Session session = createSession();
            MimeMessage mimeMessage = new MimeMessage(session);
            prepareEmailMessage(mimeMessage, to, title, html);
            Transport.send(mimeMessage);
            log.info("Email has been sent successfully");
        } catch ( Exception e ) {
            throw new MyException("send as html exception: " + e.getMessage(), ExceptionCode.EMAIL_SERVICE);
        }

    }

    private static void prepareEmailMessage(MimeMessage mimeMessage, String to, String title, String html) {
        try {
            mimeMessage.setContent(html, "text/html; charset=utf-8");
            mimeMessage.setFrom( new InternetAddress(EMAIL_ADDRESS));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject(title);
        } catch ( Exception e ) {
            throw new MyException("prepare email message exception: " + e.getMessage(), ExceptionCode.EMAIL_SERVICE);
        }
    }

    private static Session createSession() {
        var properties = new Properties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_ADDRESS, EMAIL_PASSWORD);
            }
        });
    }

}
