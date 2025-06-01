package com.zetta.currencyexchange.rest.exceptionHandler;

import com.zetta.currencyexchange.rest.exceptionHandler.strategy.ApiAuthorizationErrorStrategy;
import com.zetta.currencyexchange.rest.exceptionHandler.strategy.ApiErrorStrategy;
import com.zetta.currencyexchange.rest.exceptionHandler.strategy.DefaultErrorStrategy;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@UtilityClass
public class ApiErrorStrategyContext {
    private static final Map<Integer, ApiErrorStrategy> strategyMap = new HashMap<>();

    static {
        strategyMap.put(101, new ApiAuthorizationErrorStrategy());
        strategyMap.put(102, new ApiAuthorizationErrorStrategy());
    }

    public void handleError(int errorCode, String urlInvoked) {
        log.error("Failed to invoke external endpoint [{}] with error code [{}].", urlInvoked, errorCode);
        ApiErrorStrategy strategy = strategyMap.getOrDefault(errorCode,
                new DefaultErrorStrategy());
        strategy.handle(errorCode);
    }
}
