package com.zetta.currencyexchange.rest.exceptionHandler;

import com.zetta.currencyexchange.enums.RestApiErrorEnum;
import com.zetta.currencyexchange.exception.InternalServerErrorException;
import com.zetta.currencyexchange.exception.NoContentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApiErrorStrategyContextTest {

    @Test
    void testHandleErrorFor101() {
        int expectedCode = 101;
        // Act & Assert
        InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class,
                () -> ApiErrorStrategyContext.handleError(expectedCode, "https://test-api.com"));

        assertEquals(RestApiErrorEnum.ER_500.getDescription(), internalServerErrorException.getMessage());
        assertEquals(expectedCode, internalServerErrorException.getErrorCode());
    }

    @Test
    void testHandleErrorFor102() {
        // Act & Assert
        int expectedCode = 102;
        InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class,
                () -> ApiErrorStrategyContext.handleError(expectedCode, "https://test-api.com"));

        assertEquals(RestApiErrorEnum.ER_500.getDescription(), internalServerErrorException.getMessage());
        assertEquals(expectedCode, internalServerErrorException.getErrorCode());
    }

    @Test
    void testHandleErrorFor104() {
        // Act & Assert
        int expectedCode = 105;
        InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class,
                () -> ApiErrorStrategyContext.handleError(expectedCode, "https://test-api.com"));

        assertEquals(RestApiErrorEnum.ER_500.getDescription(), internalServerErrorException.getMessage());
        assertEquals(expectedCode, internalServerErrorException.getErrorCode());
    }

    @Test
    void testHandleErrorFor106() {
        // Act & Assert
        int expectedCode = 106;
        NoContentException noContentException = assertThrows(NoContentException.class,
                () -> ApiErrorStrategyContext.handleError(expectedCode, "https://test-api.com"));

        assertEquals(RestApiErrorEnum.ER_204.getDescription(), noContentException.getMessage());
        assertEquals(expectedCode, noContentException.getErrorCode());
    }

    @Test
    void testHandleErrorForInvalidCode() {

        // Act & Assert
        InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class,
                () -> ApiErrorStrategyContext.handleError(RestApiErrorEnum.IE_124.getCode(), "https" +
                        "://test-api.com"));

        assertEquals(RestApiErrorEnum.IE_500.getDescription(),
                internalServerErrorException.getMessage());
        assertEquals(RestApiErrorEnum.IE_500.getCode(),
                internalServerErrorException.getErrorCode());
    }
}