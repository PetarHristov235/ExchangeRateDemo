package com.zetta.currencyexchange.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Data
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeRateResponse {
    boolean success;
    Instant timestamp;
    String source;
    Map<String, BigDecimal> quotes;
    Error error;

    @Getter
    @Setter
    public static class Error {
        int code;
        String info;

    }
}
