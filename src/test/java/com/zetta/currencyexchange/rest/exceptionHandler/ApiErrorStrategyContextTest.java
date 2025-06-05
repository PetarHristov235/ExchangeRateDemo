package com.zetta.currencyexchange.rest.exceptionHandler;

import com.zetta.currencyexchange.exception.BadRequestException;
import com.zetta.currencyexchange.exception.InternalServerErrorException;
import com.zetta.currencyexchange.exception.NoContentException;
import com.zetta.currencyexchange.util.ApiErrorStrategyContext;
import org.junit.jupiter.api.Test;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApiErrorStrategyContextTest {

    @Test
    void testHandleErrorFor101() {
        int errorCode = 101;
        // Act & Assert
        InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class,
                () -> ApiErrorStrategyContext.handleError(errorCode, "https://test-api.com"));

        assertEquals(ER_500.getDescription(), internalServerErrorException.getMessage());
        assertEquals(ER_500.name(), internalServerErrorException.getErrorCode());
    }

    @Test
    void testHandleErrorFor102() {
        // Act & Assert
        int errorCode = 102;
        InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class,
                () -> ApiErrorStrategyContext.handleError(errorCode, "https://test-api.com"));

        assertEquals(ER_500.getDescription(), internalServerErrorException.getMessage());
        assertEquals(ER_500.name(), internalServerErrorException.getErrorCode());
    }

    @Test
    void testHandleErrorFor104() {
        // Act & Assert
        int errorCode = 105;
        InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class,
                () -> ApiErrorStrategyContext.handleError(errorCode, "https://test-api.com"));

        assertEquals(ER_500.getDescription(), internalServerErrorException.getMessage());
        assertEquals(ER_500.name(), internalServerErrorException.getErrorCode());
    }

    @Test
    void testHandleErrorFor106() {
        // Act & Assert
        int errorCode = 106;
        NoContentException noContentException = assertThrows(NoContentException.class,
                () -> ApiErrorStrategyContext.handleError(errorCode, "https://test-api.com"));

        assertEquals(ER_204.getDescription(), noContentException.getMessage());
        assertEquals(ER_204.name(), noContentException.getErrorCode());
    }

    @Test
    void testHandleErrorFor103() {
        // Act & Assert
        int errorCode = 103;
        InternalServerErrorException noContentException = assertThrows(InternalServerErrorException.class,
                () -> ApiErrorStrategyContext.handleError(errorCode, "https://test-api.com"));

        assertEquals(ER_124.getDescription(), noContentException.getMessage());
        assertEquals(ER_124.name(), noContentException.getErrorCode());
    }


    @Test
    void testHandleErrorForInvalidSourceCurrency() {
        // Act & Assert
        int errorCode = 201;
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> ApiErrorStrategyContext.handleError(errorCode,
                        "https" +
                                "://test-api.com"));

        assertEquals(ER_312.getDescription(),
                badRequestException.getMessage());
        assertEquals(ER_312.name(),
                badRequestException.getErrorCode());
    }


    @Test
    void testHandleErrorForInvalidCurrencyCodes() {
        // Act & Assert
        int errorCode = 202;
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> ApiErrorStrategyContext.handleError(errorCode,
                        "https" +
                                "://test-api.com"));

        assertEquals(ER_313.getDescription(),
                badRequestException.getMessage());
        assertEquals(ER_313.name(),
                badRequestException.getErrorCode());
    }

    @Test
    void testHandleErrorForInvalidCode() {

        // Act & Assert
        int errorCode = 124;
        InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class,
                () -> ApiErrorStrategyContext.handleError(errorCode, "https" +
                        "://test-api.com"));

        assertEquals(GE_509.getDescription(),
                internalServerErrorException.getMessage());
        assertEquals(GE_509.name(),
                internalServerErrorException.getErrorCode());
    }
}