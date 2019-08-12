package com.app.service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.function.Predicate;

public class UserDataService {

    private static final Scanner sc = new Scanner(System.in);
    private static final UserDataService instance = new UserDataService();
    private static final String dateFormat = "dd/MM/yyyy";

    public static UserDataService getInstance() {
        return instance;
    }

    private UserDataService() {
    }

    public String getString(String message) {
        System.out.println(message);
        return sc.nextLine();
    }

    public String getStringWithValidator(String message, Predicate<String> validator) {
        System.out.println(message);
        String value = sc.nextLine();
        if (validator.test(value)) {
            return value;
        } else {
            System.out.println("Wrong input - Try again");
            return getStringWithValidator(message, validator);
        }
    }

    public int getInt(String message) {
        System.out.println(message);
        int value;
        try {
            return value = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            throw new MyException("WRONG INT INPUT", ExceptionCode.WRONG_INPUT);
        }
    }

    public int getIntWithValidator(String message, Predicate<Integer> validator) {
        System.out.println(message);
        int value;
        try {
            value = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            throw new MyException("WRONG INT INPUT", ExceptionCode.WRONG_INPUT);
        }
        if (validator != null && validator.test(value)) {
            return value;
        } else {
            System.err.println("Please write valid value");
            return getIntWithValidator(message, validator);
        }
    }

    public BigDecimal getBigDecimal(String message, Predicate<BigDecimal> validator) {
        System.out.println(message);
        BigDecimal value;
        try {
            value = new BigDecimal(sc.nextLine());
        } catch (Exception e) {
            throw new MyException("WRONG BIGDECIMAL INPUT", ExceptionCode.WRONG_INPUT);
        }
        if (validator.test(value)) {
            return value;
        } else {
            System.err.println("Please write valid value");
            return getBigDecimal(message, validator);
        }
    }

    public boolean isMailAddress(String adress) {
        String addressRegex = "[a-z]+\\.[a-z]+@([a-z0-9]+\\.)+[a-z]+";
        return adress.matches(addressRegex);

    }

    public Date getDate(String message) {
        System.out.println("Date need to be in format dd/MM/yyyy e.g. 12/05/2018");
        String dateFromUser = getString(message);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date date;
        try {
            date = simpleDateFormat.parse(dateFromUser);
        } catch (Exception e) {
            throw new MyException("CAN NOT GET DATE FROM USER", ExceptionCode.WRONG_INPUT);
        }

        return date;
    }

    public void close() {
        sc.close();
    }
}