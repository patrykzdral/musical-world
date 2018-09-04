package com.patrykzdral.musicalworldcore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static springfox.documentation.builders.PathSelectors.any;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final Contact DEFAULT_CONTACT = new Contact(
            "Kamil Cieślik", "https://github.com/kamilcieslik", "kamil.cieslik@protonmail.com");

    private static final ApiInfo DEFAULT_API_INFO = new ApiInfoBuilder()
            .title("PlayApp")
            .description("Backend services for 'playApp' application " +
                    "(Engineer's Thesis | Wrocław University of Science and Technology | 2018)")
            .version("1.0")
            .contact(DEFAULT_CONTACT)
            .build();

    private static final Set<String> CONSUMES =
            new HashSet<>(Arrays.asList("application/json", "application/json"));

    private static final Set<String> PRODUCES =
            new HashSet<>(Collections.singletonList("application/json"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(any())
                .build()
                .produces(PRODUCES)
                .consumes(CONSUMES);
    }
}