package com.zetta.currencyexchange.rest.exceptionHandler.strategy;

import com.zetta.currencyexchange.enums.RestApiErrorEnum;
import com.zetta.currencyexchange.exception.BadRequestException;

public class ApiInvalidCurrencyCodesErrorStrategy implements ApiErrorStrategy {
    @Override
    public void handle(int errorCode) {
        throw new BadRequestException(RestApiErrorEnum.ER_313.getDescription(), errorCode);
    }
}
