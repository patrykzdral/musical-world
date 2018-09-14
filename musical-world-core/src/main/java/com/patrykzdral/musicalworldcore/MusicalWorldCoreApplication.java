package com.patrykzdral.musicalworldcore;

import com.patrykzdral.musicalworldcore.persistance.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootApplication
@Slf4j
public class MusicalWorldCoreApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(MusicalWorldCoreApplication.class, args);
    }

}
