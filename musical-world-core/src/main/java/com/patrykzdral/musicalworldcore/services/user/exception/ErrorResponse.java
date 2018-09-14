package com.patrykzdral.musicalworldcore.services.user.exception;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class ErrorResponse {
    private String errorCode;
    private String message;
    private Timestamp timestamp;

    public ErrorResponse(String errorCode, String message, Timestamp timestamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
    }
}
