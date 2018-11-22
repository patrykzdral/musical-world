package com.patrykzdral.musicalworldcore.validation.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    HttpStatus status;
    String message;
    List<String> errors;
}
