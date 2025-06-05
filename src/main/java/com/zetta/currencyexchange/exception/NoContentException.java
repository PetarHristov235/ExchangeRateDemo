package com.zetta.currencyexchange.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NoContentException extends RuntimeException {
    private String errorCode;

    public NoContentException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
