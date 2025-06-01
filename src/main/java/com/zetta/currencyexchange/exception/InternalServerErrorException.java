package com.zetta.currencyexchange.exception;

import lombok.Getter;

@Getter
public class InternalServerErrorException extends RuntimeException {
    private int errorCode;

    public InternalServerErrorException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public InternalServerErrorException(String message) {
        super(message);
    }
}
