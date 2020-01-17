package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// TODO ?????
public class DateFormatterUtil {
    private static String pattern = "yyyy-MM-dd - HH:mm";

    public static String formatDate(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }
}
