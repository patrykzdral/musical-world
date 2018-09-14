package com.patrykzdral.musicalworldcore.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public String formatLocalDate(LocalDate date) {
        String result = null;
        if (date != null) {
            result = date.format(dateFormatter);
        }
        return result;
    }

    public String nowString() {
        LocalDate localDate = LocalDate.now();
        String nowDateString = formatLocalDate(localDate);

        return nowDateString;
    }

    public LocalDate nowLocalDate() {
        LocalDate localDate = LocalDate.now();
        return localDate;
    }

    public LocalDateTime nowLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime;

    }
}
