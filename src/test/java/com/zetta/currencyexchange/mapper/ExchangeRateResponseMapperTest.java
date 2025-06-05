package com.zetta.currencyexchange.mapper;

import com.zetta.currencyexchange.model.ExchangeRateResponse;
import com.zetta.currencyexchange.model.ExchangeRateResponseDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class ExchangeRateResponseMapperTest {

    private final ExchangeRateResponseMapper mapper = Mappers.getMapper(ExchangeRateResponseMapper.class);

    @Test
    void toDto_mapsFieldsCorrectly() {
        ExchangeRateResponse response = new ExchangeRateResponse();
        HashMap<String, BigDecimal> quotes = new HashMap<>();
        quotes.put("EURUSD", new BigDecimal("1.2345"));
        response.setQuotes(quotes);
        response.setTimestamp(Instant.parse("2024-06-10T12:00:00Z"));

        String from = "EUR";
        String to = "USD";

        ExchangeRateResponseDTO dto = mapper.toDto(response, from, to);

        assertThat(dto).isNotNull();
        assertThat(dto.getTarget()).isEqualTo("USD");
        assertThat(dto.getExchangeRate()).isEqualByComparingTo("1.2345");
        assertThat(dto.getTimestamp()).isEqualTo(OffsetDateTime.ofInstant(response.getTimestamp(), ZoneId.systemDefault()));
    }

    @Test
    void getExchangeRate_returnsNull_whenResponseOrQuotesNull() {
        assertThat(mapper.getExchangeRate(null, "EUR", "USD")).isNull();
        assertThat(mapper.getExchangeRate(new ExchangeRateResponse(), "EUR", "USD")).isNull();
    }

    @Test
    void getExchangeRate_returnsNull_whenKeyNotFound() {
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setQuotes(new HashMap<>());
        assertThat(mapper.getExchangeRate(response, "EUR", "USD")).isNull();
    }
}