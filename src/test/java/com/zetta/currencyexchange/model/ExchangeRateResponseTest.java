package com.zetta.currencyexchange.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExchangeRateResponseTest {

    @Test
    void testExchangeRateResponseFields() {
        Map<String, BigDecimal> quotes = new HashMap<>();
        quotes.put("USDGBP", new BigDecimal("0.79"));

        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setSuccess(true);
        response.setTimestamp(OffsetDateTime.of(LocalDateTime.of(2021, 5, 17, 12, 0), ZoneOffset.UTC).toInstant());
        response.setSource("USD");
        response.setQuotes(quotes);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getTimestamp()).isEqualTo(OffsetDateTime.of(LocalDate.of(2021, 5, 17), LocalTime.of(12, 0), ZoneOffset.UTC).toInstant());
        assertThat(response.getSource()).isEqualTo("USD");
        assertThat(response.getQuotes()).isNotNull();
        assertThat(response.getQuotes()).containsEntry("USDGBP", new BigDecimal("0.79"));
    }

    @Test
    void testExchangeRateResponseError() {
        ExchangeRateResponse.Error error = new ExchangeRateResponse.Error();
        error.setCode(101);
        error.setInfo("Invalid access key");

        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setError(error);

        assertThat(response.getError()).isNotNull();
        assertThat(response.getError().getCode()).isEqualTo(101);
        assertThat(response.getError().getInfo()).isEqualTo("Invalid access key");
    }

    @Test
    void testEqualsAndHashCode() {
        Map<String, BigDecimal> quotes = new HashMap<>();
        quotes.put("USDEUR", new BigDecimal("0.92"));

        ExchangeRateResponse r1 = new ExchangeRateResponse();
        r1.setSuccess(true);
        r1.setTimestamp(OffsetDateTime.of(LocalDateTime.of(2021, 5, 17, 12, 0), ZoneOffset.UTC).toInstant());
        r1.setSource("USD");
        r1.setQuotes(quotes);

        ExchangeRateResponse r2 = new ExchangeRateResponse();
        r2.setSuccess(true);
        r2.setTimestamp(OffsetDateTime.of(LocalDateTime.of(2021, 5, 17, 12, 0), ZoneOffset.UTC).toInstant());
        r2.setSource("USD");
        r2.setQuotes(quotes);

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }
}
