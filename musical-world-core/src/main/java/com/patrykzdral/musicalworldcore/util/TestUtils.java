package com.patrykzdral.musicalworldcore.util;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

@Component
public class TestUtils {

    public static Instant getInstant(int year, int month, int day, int hour, int minute, int second){

        return Instant.parse(year+"-"+month+"-"+day+"T"+hour+":"+minute+":"+second+"Z");
    }

    public static Date getZonedTime(int year, int month, int day, int hour, int minute, int second){

        return Date.from(Instant.parse(year+"-"+month+"-"+day+"T"+hour+":"+minute+":"+second+"Z"));
    }
}
