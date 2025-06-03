package com.zetta.currencyexchange.rest.exceptionHandler;

import com.zetta.currencyexchange.exception.BadRequestException;
import com.zetta.currencyexchange.exception.InternalServerErrorException;
import com.zetta.currencyexchange.exception.NoContentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerImplTest {
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleInternalServerErrorException_shouldReturn500() {
        // Arrange
        String message = "Internal error occurred";
        int errorCode = 500;
        InternalServerErrorException ex = new InternalServerErrorException(message, errorCode);

        // Act
        ResponseEntity<String> response = exceptionHandler.handleExchangeRateException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(message);
    }

    @Test
    void handleNoContentException_shouldReturn204() {
        // Arrange
        String message = "No content available";
        int errorCode = 204;
        NoContentException ex = new NoContentException(message, errorCode);

        // Act
        ResponseEntity<String> response = exceptionHandler.handleExchangeRateException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isEqualTo(message);
    }

    @Test
    void handleBadRequestException_shouldReturn400WithErrorMessage() {
        // Arrange
        String message = "Invalid input data";
        int errorCode = 400;
        BadRequestException ex = new BadRequestException(message, errorCode);

        // Act
        ResponseEntity<ErrorMessage> response = exceptionHandler.handleBadRequestException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo(message);
        }
}