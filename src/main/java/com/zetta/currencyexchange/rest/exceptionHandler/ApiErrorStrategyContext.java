package com.zetta.currencyexchange.rest.exceptionHandler;

import com.zetta.currencyexchange.rest.exceptionHandler.strategy.ApiErrorStrategy;
import com.zetta.currencyexchange.rest.exceptionHandler.strategy.DefaultErrorStrategy;

import java.util.HashMap;
import java.util.Map;

public class ApiErrorStrategyContext {
    private static final Map<Integer, ApiErrorStrategy> strategyMap = new HashMap<>();

    static {

    }

    public static void handleError(int errorCode) {
        ApiErrorStrategy strategy = strategyMap.getOrDefault(errorCode, new DefaultErrorStrategy());
        strategy.handle(errorCode);
    }
}
