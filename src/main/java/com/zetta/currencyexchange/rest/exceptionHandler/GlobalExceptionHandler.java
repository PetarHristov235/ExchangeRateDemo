package com.zetta.currencyexchange.rest.exceptionHandler;

import com.zetta.currencyexchange.exception.BadRequestException;
import com.zetta.currencyexchange.exception.InternalServerErrorException;
import com.zetta.currencyexchange.exception.NoContentException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<String> handleExchangeRateException(InternalServerErrorException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<String> handleExchangeRateException(NoContentException ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(BadRequestException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }
}
