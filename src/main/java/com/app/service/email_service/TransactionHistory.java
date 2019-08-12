package com.app.service.email_service;

import com.app.model.Customer;

import javax.mail.Message;

public class TransactionHistory extends EmailService {

    public TransactionHistory(Customer customer) {
        super(customer);
    }

    @Override
    public void sendEmail() {

    }

    @Override
    protected Message prepareMessage() {
        return null;
    }
}
