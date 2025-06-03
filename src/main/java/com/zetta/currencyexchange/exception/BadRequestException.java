package com.zetta.currencyexchange.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BadRequestException extends RuntimeException {
    int errorCode;

    public BadRequestException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
