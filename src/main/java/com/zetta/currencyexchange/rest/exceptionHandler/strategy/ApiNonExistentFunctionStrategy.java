package com.zetta.currencyexchange.rest.exceptionHandler.strategy;

import com.zetta.currencyexchange.enums.RestApiErrorEnum;
import com.zetta.currencyexchange.exception.InternalServerErrorException;

public class ApiNonExistentFunctionStrategy implements ApiErrorStrategy {
    @Override
    public void handle(int errorCode) {
        throw new InternalServerErrorException(RestApiErrorEnum.IE_124.getDescription(),
                errorCode);
    }
}
