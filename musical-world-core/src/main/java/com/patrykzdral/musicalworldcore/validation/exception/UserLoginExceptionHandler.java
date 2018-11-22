package com.patrykzdral.musicalworldcore.validation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;

@ControllerAdvice
public class UserLoginExceptionHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler({InvalidGrantException.class})
    public ResponseEntity<Object> handleInternalException(InvalidGrantException invalidGrantException) {
        logger.info("Ouath 2 exception handled");
        var response = new ErrorResponse(invalidGrantException.getOAuth2ErrorCode(), invalidGrantException.getMessage(), Timestamp.from(Instant.now()));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
