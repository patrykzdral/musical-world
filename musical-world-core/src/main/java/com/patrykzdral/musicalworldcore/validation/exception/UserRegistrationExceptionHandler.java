package com.patrykzdral.musicalworldcore.validation.exception;

import com.patrykzdral.musicalworldcore.util.TimestampUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.time.Instant;

@ControllerAdvice
public class UserRegistrationExceptionHandler extends ResponseEntityExceptionHandler {

    private TimestampUtils timestampUtils;

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<Object> handleInternalException(ApplicationException applicationException) {
        logger.error("400 Status Code", applicationException);
        var response = new ErrorResponse(applicationException.getErrorCode().toString(), applicationException.getMessage(), Timestamp.from(Instant.now()));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MessagingException.class})
    public ResponseEntity<Object> handleMessageException(MessagingException message) {
        logger.error("400 Status Code", message);
        var response = new ErrorResponse("Mail sending error", message.getMessage(), Timestamp.from(Instant.now()));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
