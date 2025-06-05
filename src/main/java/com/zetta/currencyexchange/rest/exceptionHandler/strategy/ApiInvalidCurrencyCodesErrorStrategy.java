package com.zetta.currencyexchange.rest.exceptionHandler.strategy;

import com.zetta.currencyexchange.exception.BadRequestException;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.ER_313;

public class ApiInvalidCurrencyCodesErrorStrategy implements ApiErrorStrategy {
    @Override
    public void handle() {
        throw new BadRequestException(ER_313.getDescription(),
                ER_313.name());
    }
}
