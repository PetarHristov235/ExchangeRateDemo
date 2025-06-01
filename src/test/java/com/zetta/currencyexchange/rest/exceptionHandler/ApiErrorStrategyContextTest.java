package com.zetta.currencyexchange.rest.exceptionHandler;

import com.zetta.currencyexchange.enums.RestApiErrorEnum;
import com.zetta.currencyexchange.exception.InternalServerErrorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApiErrorStrategyContextTest {

    @Test
    void testHandleErrorFor101() {
        // Act & Assert
        InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class,
                () -> ApiErrorStrategyContext.handleError(101, "https://test-api.com"));

        assertEquals(RestApiErrorEnum.ER_500.getDescription(), internalServerErrorException.getMessage());
        assertEquals(RestApiErrorEnum.ER_500.getCode(), internalServerErrorException.getErrorCode());
    }

    @Test
    void testHandleErrorFor102() {
        // Act & Assert
        InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class,
                () -> ApiErrorStrategyContext.handleError(102, "https://test-api.com"));

        assertEquals(RestApiErrorEnum.ER_500.getDescription(), internalServerErrorException.getMessage());
        assertEquals(RestApiErrorEnum.ER_500.getCode(), internalServerErrorException.getErrorCode());
    }

    @Test
    void testHandleErrorForInvalidCode() {
        // Act & Assert
        InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class,
                () -> ApiErrorStrategyContext.handleError(999, "https://test-api.com"));

        assertEquals(RestApiErrorEnum.IE_500.getDescription(), internalServerErrorException.getMessage());
        assertEquals(RestApiErrorEnum.IE_500.getCode(), internalServerErrorException.getErrorCode());
    }
}