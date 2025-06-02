package com.zetta.currencyexchange.rest.exceptionHandler.strategy;

import com.zetta.currencyexchange.enums.RestApiErrorEnum;
import com.zetta.currencyexchange.exception.NoContentException;

public class ApiNoContentErrorStrategy implements ApiErrorStrategy {
    @Override
    public void handle(int errorCode) {
        throw new NoContentException(RestApiErrorEnum.ER_204.getDescription(), errorCode);
    }
}
