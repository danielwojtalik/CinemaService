package com.app.service.email_service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.service.property_service.PropertiesService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public abstract class EmailService {


    protected final static Properties properties = new Properties();
    protected String emailFrom;
    protected String password;
    protected String emailContent;
    protected String emailTitle;

    protected Session session;
    protected Customer customer;

    static {
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
    }

    public EmailService(Customer customer) {
        PropertiesService propertiesService = new PropertiesService("src\\main\\resources\\private_config.properties");
        this.emailFrom = propertiesService.loadProperty("email.user");
        this.password = propertiesService.loadProperty("password");
        this.customer = customer;
        this.session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, password);
            }
        });
    }
    protected abstract String initializeEmailContent();

    protected abstract String initializeEmailTitle();

    protected Message prepareMessage() {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(emailFrom));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(customer.getEmail()));
            message.setSubject(emailTitle);
            message.setText(emailContent);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("CAN NOT PREPARE MESSAGE", ExceptionCode.EMAIL_SERVICE);
        }
    }

    public void sendEmail() {
        try {
            Message message = prepareMessage();
            Transport.send(message);
            System.out.println("Message sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("CAN NOT SEND EMAIL", ExceptionCode.EMAIL_SERVICE);
        }
    }
}
