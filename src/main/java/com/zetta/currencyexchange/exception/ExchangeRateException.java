package com.zetta.currencyexchange.exception;

public class ExchangeRateException extends RuntimeException {
    public ExchangeRateException() {
    }

    public ExchangeRateException(String message) {
        super(message);
    }
}
