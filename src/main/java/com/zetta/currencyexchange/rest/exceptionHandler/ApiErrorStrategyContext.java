package com.zetta.currencyexchange.rest.exceptionHandler;

import com.zetta.currencyexchange.enums.RestApiErrorEnum;
import com.zetta.currencyexchange.rest.exceptionHandler.strategy.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@UtilityClass
public class ApiErrorStrategyContext {
    private static final Map<Integer, ApiErrorStrategy> strategyMap = new HashMap<>();

    static {
//        Authorization errors
        strategyMap.put(101, new ApiAuthorizationErrorStrategy());
        strategyMap.put(102, new ApiAuthorizationErrorStrategy());
        strategyMap.put(104, new ApiAuthorizationErrorStrategy());
        strategyMap.put(105, new ApiAuthorizationErrorStrategy());

//        Non-existing external API function
        strategyMap.put(103, new ApiNonExistentFunctionStrategy());

//        No Content
        strategyMap.put(106, new ApiNoContentErrorStrategy());

    }

    public void handleError(int errorCode, String urlInvoked) {
        log.error("Failed to invoke external endpoint [{}] with error code [{}].", urlInvoked, errorCode);
        ApiErrorStrategy strategy = strategyMap.getOrDefault(errorCode,
                new DefaultErrorStrategy());
        strategy.handle(errorCode);
    }
}
