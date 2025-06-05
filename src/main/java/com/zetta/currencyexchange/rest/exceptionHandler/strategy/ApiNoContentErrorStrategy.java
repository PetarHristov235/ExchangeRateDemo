package com.zetta.currencyexchange.rest.exceptionHandler.strategy;

import com.zetta.currencyexchange.exception.NoContentException;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.ER_204;

public class ApiNoContentErrorStrategy implements ApiErrorStrategy {
    @Override
    public void handle() {
        throw new NoContentException(ER_204.getDescription(), ER_204.name());
    }
}
