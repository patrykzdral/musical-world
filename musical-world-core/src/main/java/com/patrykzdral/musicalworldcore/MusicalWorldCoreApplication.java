package com.patrykzdral.musicalworldcore;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
@Slf4j
public class MusicalWorldCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusicalWorldCoreApplication.class, args);
    }
}
