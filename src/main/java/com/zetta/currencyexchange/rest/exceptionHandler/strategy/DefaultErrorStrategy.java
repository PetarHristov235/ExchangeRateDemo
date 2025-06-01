package com.zetta.currencyexchange.rest.exceptionHandler.strategy;

import com.zetta.currencyexchange.enums.RestApiErrorEnum;
import com.zetta.currencyexchange.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultErrorStrategy implements ApiErrorStrategy {
    @Override
    public void handle(int errorCode) {
        log.error("An unknown error occurred: errorCode: {}", errorCode);
        throw new InternalServerErrorException(RestApiErrorEnum.IE_500.getDescription(), RestApiErrorEnum.IE_500.getCode());
    }
}
