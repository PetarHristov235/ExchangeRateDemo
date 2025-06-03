package com.zetta.currencyexchange.validation;

import com.zetta.currencyexchange.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidateCurrencyConversionHistoryImplTest {

    private ValidateCurrencyConversionHistoryRequestImpl validator;

    @BeforeEach
    void setUp() {
        validator = new ValidateCurrencyConversionHistoryRequestImpl();
    }

    @Test
    void shouldThrowExceptionWhenAllParamsAreNullOrEmpty() {
        assertThrows(BadRequestException.class, () ->
                validator.validateRequest(null, null, null));
        assertThrows(BadRequestException.class, () ->
                validator.validateRequest("", null, null));
    }

    @Test
    void shouldThrowExceptionWhenTransactionIdIsInvalidUUID() {
        assertThrows(BadRequestException.class, () ->
                validator.validateRequest("123", null, null)); // invalid UUID
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