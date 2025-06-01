package com.zetta.currencyexchange.rest.exceptionHandler.strategy;

import com.zetta.currencyexchange.enums.RestApiErrorEnum;
import com.zetta.currencyexchange.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiAuthorizationErrorStrategy implements ApiErrorStrategy {
    @Override
    public void handle(int errorCode) {
        throw new InternalServerErrorException(RestApiErrorEnum.ER_500.getDescription(),
                RestApiErrorEnum.ER_500.getCode());
    }
}
