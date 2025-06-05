package com.zetta.currencyexchange.rest.exceptionHandler.strategy;

import com.zetta.currencyexchange.enums.RestApiErrorEnum;
import com.zetta.currencyexchange.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.GE_509;

public class DefaultErrorStrategy implements ApiErrorStrategy {
    @Override
    public void handle() {
        throw new InternalServerErrorException(GE_509.getDescription(),
            GE_509.name());
    }
}
