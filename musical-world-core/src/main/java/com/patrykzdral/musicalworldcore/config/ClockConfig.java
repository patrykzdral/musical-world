package com.patrykzdral.musicalworldcore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfig {
    @Bean
    public Clock globalClock() {
        return Clock.systemUTC();
    }
}
