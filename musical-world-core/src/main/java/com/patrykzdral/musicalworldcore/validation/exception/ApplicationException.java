package com.patrykzdral.musicalworldcore.validation.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    public ExceptionCode errorCode;

    public ApplicationException(ExceptionCode title, String message) {
        super(message);
        this.errorCode = title;
    }
}
