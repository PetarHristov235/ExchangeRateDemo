package com.zetta.currencyexchange.mapper;

import com.zetta.currencyexchange.model.ExchangeRateResponse;
import com.zetta.currencyexchange.model.ExchangeRateResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface ExchangeRateResponseMapper {
    @Mapping(target = "target", source = "to")
    @Mapping(target = "exchangeRate", expression = "java(getExchangeRate(response, from, to))")
    @Mapping(target = "timestamp", expression = "java(getTimestamp(response.getTimestamp()))")
    ExchangeRateResponseDTO toDto(ExchangeRateResponse response, String from, String to);

    default BigDecimal getExchangeRate(ExchangeRateResponse response, String from, String to) {
        if (response == null || response.getQuotes() == null) return null;
        return response.getQuotes().get(from.toUpperCase().concat(to.toUpperCase()));
    }

    default OffsetDateTime getTimestamp(Instant timestamp) {
        if (timestamp != null) {
            return OffsetDateTime.ofInstant(timestamp, ZoneId.systemDefault());
        }
        return null;
    }
}