package com.app.service.email_service;

import com.app.model.Customer;
import com.app.service.property_service.PropertiesService;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;


public abstract class EmailService {


    protected final static Properties properties = new Properties();
    protected  String emailFrom;
    protected  String password;

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

    public abstract void sendEmail();

    protected abstract Message prepareMessage();
}
