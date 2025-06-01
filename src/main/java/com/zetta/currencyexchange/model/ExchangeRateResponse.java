package com.zetta.currencyexchange.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ExchangeRateResponse {
    private boolean success;
    private long timestamp;
    private String source;
    private Map<String, BigDecimal> quotes;
    private Error error;

    @Getter
    @Setter
    public static class Error {
        private int code;
        private String info;

    }
}
