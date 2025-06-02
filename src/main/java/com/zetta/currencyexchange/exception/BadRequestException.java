package com.zetta.currencyexchange.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private int errorCode;

    public BadRequestException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
