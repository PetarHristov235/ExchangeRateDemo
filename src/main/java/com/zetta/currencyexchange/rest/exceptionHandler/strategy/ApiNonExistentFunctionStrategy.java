package com.zetta.currencyexchange.rest.exceptionHandler.strategy;

import com.zetta.currencyexchange.exception.InternalServerErrorException;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.ER_124;

public class ApiNonExistentFunctionStrategy implements ApiErrorStrategy {
    @Override
    public void handle() {
        throw new InternalServerErrorException(ER_124.getDescription(),
                ER_124.name());
    }
}
