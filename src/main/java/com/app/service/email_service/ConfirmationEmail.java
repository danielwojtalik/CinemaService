package com.app.service.email_service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.model.Movie;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfirmationEmail extends EmailService {

    private final static String MESSAGE_CONTENT = "Hello, %s %s!\nYou have already successfully bought ticket for movie: %s.\n" +
            "The movie starts at: %s.\nThe ticket price is equal: %s zł.";
    private Movie movie;
    private Date startTime;


    public ConfirmationEmail(Customer customer, Movie movie, Date startTime) {
        super(customer);
        this.movie = movie;
        this.startTime = startTime;
    }

    @Override
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

    @Override
    protected Message prepareMessage() {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(emailFrom));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(customer.getEmail()));
            message.setSubject("Confirmation");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm - dd/MM");
            String messageContent = String.format(MESSAGE_CONTENT, customer.getName(), customer.getSurname(), movie.getTitle(),
                    simpleDateFormat.format(startTime), movie.getPrice());
            message.setText(messageContent);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("CAN NOT PREPARE MESSAGE", ExceptionCode.EMAIL_SERVICE);
        }
    }
}
