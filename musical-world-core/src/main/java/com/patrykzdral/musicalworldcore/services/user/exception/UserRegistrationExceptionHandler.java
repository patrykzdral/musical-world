package com.patrykzdral.musicalworldcore.services.user.exception;

import com.patrykzdral.musicalworldcore.util.TimestampUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class UserRegistrationExceptionHandler extends ResponseEntityExceptionHandler {

    private TimestampUtils timestampUtils;

    @ExceptionHandler({InternalException.class})
    public ResponseEntity<Object> handleInternalException(InternalException internalException) {
        logger.error("400 Status Code", internalException);
        var response = new ErrorResponse(internalException.getErrorCode(), internalException.getMessage(), Timestamp.from(Instant.now()));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
