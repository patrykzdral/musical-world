package com.patrykzdral.musicalworldcore.data_parse_test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class DateTest {

    @Test
    public void shouldDateBeParsed(){
        String dateFrom= "Sun Oct 28 2018 14:38:58 GMT 0100 (Central European Standard Time)";

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)");
        LocalDate date = LocalDate.parse("");
        log.info(date.toString());
    }
}
