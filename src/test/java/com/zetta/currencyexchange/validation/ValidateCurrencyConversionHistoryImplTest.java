package com.zetta.currencyexchange.validation;

import com.zetta.currencyexchange.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.CH_400;
import static com.zetta.currencyexchange.enums.RestApiErrorEnum.CH_401;
import static org.junit.jupiter.api.Assertions.*;

class ValidateCurrencyConversionHistoryImplTest {

    private ValidateCurrencyConversionHistoryRequestImpl validator;

    @BeforeEach
    void setUp() {
        validator = new ValidateCurrencyConversionHistoryRequestImpl();
    }

    @Test
    void shouldThrowExceptionWhenAllParamsAreNullOrEmpty() {
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () ->
                validator.validateRequest(null, null, null));
        assertEquals(badRequestException.getErrorCode(), CH_400.name());
        BadRequestException secondBadRequestException = assertThrows(BadRequestException.class,
                () ->
                validator.validateRequest("", null, null));
        assertEquals(secondBadRequestException.getErrorCode(), CH_400.name());
    }

    @Test
    void shouldThrowExceptionWhenTransactionIdIsInvalidUUID() {
        assertThrows(BadRequestException.class, () ->
                validator.validateRequest("123", null, null)); // invalid UUID
    }

    @Test
    void shouldThrowExceptionWhenTransactionFromIsAfterTransactionTo() {
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () ->
                validator.validateRequest(null, OffsetDateTime.now(),
                        OffsetDateTime.now().minusDays(1)));//
        assertEquals(badRequestException.getErrorCode(), CH_401.name());
// invalid UUID
    }

    @Test
    void shouldNotThrowExceptionWhenTransactionIdIsValidUUID() {
        assertDoesNotThrow(() ->
                validator.validateRequest("550e8400-e29b-41d4-a716-446655440000", null, null));
    }

    @Test
    void shouldNotThrowExceptionWhenTransactionDateFromIsPresent() {
        assertDoesNotThrow(() ->
                validator.validateRequest(null, OffsetDateTime.now(), null));
    }

    @Test
    void shouldNotThrowExceptionWhenTransactionDateToIsPresent() {
        assertDoesNotThrow(() ->
                validator.validateRequest(null, null, OffsetDateTime.now()));
    }
}