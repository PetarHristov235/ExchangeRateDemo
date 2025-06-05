package com.zetta.currencyexchange.rest.exceptionHandler.strategy;

import com.zetta.currencyexchange.exception.InternalServerErrorException;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.ER_500;

public class ApiAuthorizationErrorStrategy implements ApiErrorStrategy {
    @Override
    public void handle() {
        throw new InternalServerErrorException(ER_500.getDescription(),
                ER_500.name());
    }
}
