package com.zetta.currencyexchange.rest.exceptionHandler;

import com.zetta.currencyexchange.exception.BadRequestException;
import com.zetta.currencyexchange.exception.InternalServerErrorException;
import com.zetta.currencyexchange.exception.NoContentException;
import com.zetta.currencyexchange.model.ApplicationApiException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.Set;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerImplTest {
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void givenInternalServerErrorException_whenHandleExchangeRateException_thenReturns500() {
        // Arrange
        InternalServerErrorException ex = new InternalServerErrorException(ER_500.getDescription(), ER_500.name());

        // Expected
        ApplicationApiException expectedApplicationApiException = createApplicationApiException(ER_500.getDescription(), ER_500.name());

        // Act
        ResponseEntity<ApplicationApiException> response = exceptionHandler.handleExchangeRateException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(expectedApplicationApiException);
    }

    @Test
    void givenNoContentException_whenHandleExchangeRateException_thenReturns204() {
        // Arrange
        NoContentException ex = new NoContentException(ER_204.getDescription(), ER_204.name());

        // Expected
        ApplicationApiException expectedApplicationApiException = createApplicationApiException(ER_204.getDescription(), ER_204.name());

        // Act
        ResponseEntity<ApplicationApiException> response = exceptionHandler.handleExchangeRateException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isEqualTo(expectedApplicationApiException);
    }

    @Test
    void givenBadRequestException_whenHandleBadRequestException_thenReturns400WithErrorMessage() {
        // Arrange
        BadRequestException ex = new BadRequestException(ER_313.getDescription(), ER_313.name());

        // Expected
        ApplicationApiException expectedApplicationApiException = createApplicationApiException(ER_313.getDescription(), ER_313.name());

        // Act
        ResponseEntity<ApplicationApiException> response = exceptionHandler.handleBadRequestException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(expectedApplicationApiException);
    }

    @Test
    void givenConstraintViolationException_whenHandle_thenReturns400() {
        // Arrange
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("field");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be blank");
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
        ConstraintViolationException ex = new ConstraintViolationException(violations);

        // Act
        ResponseEntity<ApplicationApiException> response = exceptionHandler.handleConstraintViolationException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getCode()).isEqualTo(GE_400.name());
        assertThat(response.getBody().getMessage()).contains("field: must not be blank");
    }

    @Test
    void givenHttpMessageConversionException_whenHandle_thenReturns400() {
        HttpMessageConversionException ex = new HttpMessageConversionException("Invalid message");

        ResponseEntity<ApplicationApiException> response = exceptionHandler.handleHttpMessageNotReadableException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getCode()).isEqualTo(GE_423.name());
        assertThat(response.getBody().getMessage()).isEqualTo(GE_423.getDescription());
    }

    @Test
    void givenMethodArgumentTypeMismatchException_whenHandle_thenReturns400() {
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "value", String.class, "param", null, null);

        ResponseEntity<ApplicationApiException> response = exceptionHandler.handleMethodArgumentTypeMismatchException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getCode()).isEqualTo(GE_424.name());
        assertThat(response.getBody().getMessage()).isEqualTo(String.format(GE_424.getDescription(),
                "param", "String"));
    }

    @Test
    void givenHttpRequestMethodNotSupportedException_whenHandle_thenReturns405() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST");

        ResponseEntity<ApplicationApiException> response = exceptionHandler.handleHttpRequestMethodNotSupportedException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(response.getBody().getCode()).isEqualTo(GE_405.name());
        assertThat(response.getBody().getMessage()).contains(String.format(GE_405.getDescription(), "POST"));
    }

    @Test
    void givenHttpMediaTypeNotSupportedException_whenHandle_thenReturns415() {
        HttpMediaTypeNotSupportedException ex = new HttpMediaTypeNotSupportedException(
                MediaType.APPLICATION_XML,
                Collections.singletonList(MediaType.APPLICATION_JSON));

        ResponseEntity<ApplicationApiException> response = exceptionHandler.handleHttpMediaTypeNotSupportedException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        assertThat(response.getBody().getCode()).isEqualTo(GE_415.name());
        assertThat(response.getBody().getMessage()).contains(String.format(GE_415.getDescription(),
                "application/xml"));
    }

    @Test
    void givenGenericException_whenHandle_thenReturns500() {
        Exception ex = new Exception("Unexpected error");

        ResponseEntity<ApplicationApiException> response = exceptionHandler.handleGenericException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getCode()).isEqualTo(GE_500.name());
        assertThat(response.getBody().getMessage()).contains("Unexpected error");
    }

    private ApplicationApiException createApplicationApiException(String message, String errorCode) {
        ApplicationApiException applicationApiException = new ApplicationApiException();
        applicationApiException.setMessage(message);
        applicationApiException.setCode(errorCode);
        return applicationApiException;
    }
}