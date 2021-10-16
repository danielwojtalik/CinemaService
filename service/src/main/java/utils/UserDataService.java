package utils;


import exceptions.ExceptionCode;
import exceptions.CustomException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.Predicate;

public final class UserDataService {

    private static Scanner sc = new Scanner(System.in);
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private UserDataService() {
    }

    public static String getString(String message) {
        System.out.println(message);
        return sc.nextLine();
    }

    public static String getStringWithValidator(String message, Predicate<String> validator) {
        System.out.println(message);
        String value = sc.nextLine();
        if (validator.test(value)) {
            return value;
        } else {
            System.out.println("Wrong input - Try again");
            return getStringWithValidator(message, validator);
        }
    }

    public static int getInt(String message) {
        System.out.println(message);
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            throw new CustomException("WRONG INT INPUT", ExceptionCode.USER_DATA_SERVICE);
        }
    }

    public static int getIntWithValidator(String message, Predicate<Integer> validator) {
        System.out.println(message);
        int value;
        try {
            value = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            throw new CustomException("WRONG INT INPUT", ExceptionCode.USER_DATA_SERVICE);
        }
        if (validator != null && validator.test(value)) {
            return value;
        } else {
            System.err.println("Please write valid value");
            return getIntWithValidator(message, validator);
        }
    }

    public static BigDecimal getBigDecimal(String message, Predicate<BigDecimal> validator) {
        System.out.println(message);
        BigDecimal value;
        try {
            value = new BigDecimal(sc.nextLine());
        } catch (Exception e) {
            throw new CustomException("WRONG BIGDECIMAL INPUT", ExceptionCode.USER_DATA_SERVICE);
        }
        if (validator.test(value)) {
            return value;
        } else {
            System.err.println("Please write valid value");
            return getBigDecimal(message, validator);
        }
    }

    public static boolean isMailAddress(String adress) {
        String addressRegex = "[a-z]+\\.?[a-z]+@([a-z0-9]+\\.)+[a-z]+";
        return adress.matches(addressRegex);

    }

    public static LocalDate getDate(String message) {
        System.out.println("Date need to be in format dd/MM/yyyy e.g. 12/05/2018");
        String dateFromUser = getString(message);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate localDate;

        try {
            localDate = LocalDate.parse(dateFromUser, formatter);
        } catch (Exception e) {
            throw new CustomException("CAN NOT GET DATE FROM USER", ExceptionCode.USER_DATA_SERVICE);
        }
        return localDate;
    }

    public static void close() {
        if (sc != null) {
            sc.close();
            sc = null;
        }
    }

    public static boolean makeDecision(String message) {
        System.out.println(message);
        boolean repeat = true;
        while (repeat) {
            String decision = getString("Y\\N");
            switch (decision) {
                case "Y" -> {
                    return true;
                }
                case "N" -> {
                    return false;
                }
            }
            System.err.println("Try again - please write \"Y\" for yes OR \"N\" for now");
        }
        return false;
    }
}