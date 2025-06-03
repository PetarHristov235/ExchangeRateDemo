package com.zetta.currencyexchange.mapper;

import com.zetta.currencyexchange.model.ExchangeRateResponse;
import com.zetta.currencyexchange.model.ExchangeRateResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ExchangeRateResponseMapper {
    @Mapping(target = "target", source = "to")
    @Mapping(target = "exchangeRate", expression = "java(getExchangeRate(response, from, to))")
    ExchangeRateResponseDTO toDto(ExchangeRateResponse response, String from, String to);

    default BigDecimal getExchangeRate(ExchangeRateResponse response, String from, String to) {
        if (response == null || response.getQuotes() == null) return null;
        return response.getQuotes().get(from.toUpperCase().concat(to.toUpperCase()));
    }
}