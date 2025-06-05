package com.zetta.currencyexchange.rest.exceptionHandler.strategy;

import com.zetta.currencyexchange.exception.BadRequestException;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.ER_312;

public class ApiInvalidSourceCurrencyErrorStrategy implements ApiErrorStrategy {
    @Override
    public void handle() {
        throw new BadRequestException(ER_312.getDescription(), ER_312.name());
    }
}
