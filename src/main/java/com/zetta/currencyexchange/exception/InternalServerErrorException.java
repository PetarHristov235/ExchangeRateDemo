package com.zetta.currencyexchange.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class InternalServerErrorException extends RuntimeException {
    String errorCode;

    public InternalServerErrorException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
