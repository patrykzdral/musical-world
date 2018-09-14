package com.patrykzdral.musicalworldcore.services.user.exception;

import lombok.Getter;

@Getter
public class InternalException extends RuntimeException {

    public String errorCode;

    public InternalException(String title, String message) {
        super(message);
        this.errorCode = title;
    }
}
