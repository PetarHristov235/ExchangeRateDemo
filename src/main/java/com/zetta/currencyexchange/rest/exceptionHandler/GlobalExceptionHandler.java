package com.zetta.currencyexchange.rest.exceptionHandler;

import com.zetta.currencyexchange.exception.BadRequestException;
import com.zetta.currencyexchange.exception.InternalServerErrorException;
import com.zetta.currencyexchange.exception.NoContentException;
import com.zetta.currencyexchange.model.ApplicationApiException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

import static com.zetta.currencyexchange.enums.RestApiErrorEnum.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ApplicationApiException> handleExchangeRateException(InternalServerErrorException ex) {
        log.error("InternalServerErrorException: {}", ex.getMessage(), ex);
        ApplicationApiException applicationApiException = new ApplicationApiException();
        applicationApiException.setCode(ex.getErrorCode());
        applicationApiException.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(applicationApiException);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<ApplicationApiException> handleExchangeRateException(NoContentException ex) {
        log.error("NoContentException: {}", ex.getMessage(), ex);
        ApplicationApiException applicationApiException = new ApplicationApiException();
        applicationApiException.setCode(ex.getErrorCode());
        applicationApiException.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(applicationApiException);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApplicationApiException> handleBadRequestException(BadRequestException ex) {
        log.error("BadRequestException: {}", ex.getMessage(), ex);
        ApplicationApiException applicationApiException = new ApplicationApiException();
        applicationApiException.setCode(ex.getErrorCode());
        applicationApiException.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(applicationApiException);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationApiException> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException: {}", ex.getMessage(), ex);
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ApplicationApiException apiException = new ApplicationApiException();
        apiException.setCode(GE_400.name());
        apiException.setMessage(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApplicationApiException> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("ConstraintViolationException: {}", ex.getMessage(), ex);
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));
        ApplicationApiException apiException = new ApplicationApiException();
        apiException.setCode(GE_400.name());
        apiException.setMessage(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ApplicationApiException> handleHttpMessageNotReadableException(HttpMessageConversionException ex) {
        log.error("HttpMessageConversionException: {}", ex.getMessage(), ex);
        ApplicationApiException apiException = new ApplicationApiException();
        apiException.setCode(GE_423.name());
        apiException.setMessage(GE_423.getDescription());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApplicationApiException> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatchException: {}", ex.getMessage(), ex);
        ApplicationApiException apiException = new ApplicationApiException();
        apiException.setCode(GE_424.name());
        apiException.setMessage(String.format(GE_424.getDescription(), ex.getName(),
                ex.getRequiredType() == null ? "unknown" : ex.getRequiredType().getSimpleName()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApplicationApiException> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("HttpRequestMethodNotSupportedException: {}", ex.getMessage(), ex);
        ApplicationApiException apiException = new ApplicationApiException();
        apiException.setCode(GE_405.name());
        apiException.setMessage(String.format(GE_405.getDescription(), ex.getMethod()));
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(apiException);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApplicationApiException> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.error("HttpMediaTypeNotSupportedException: {}", ex.getMessage(), ex);
        ApplicationApiException apiException = new ApplicationApiException();
        apiException.setCode(GE_415.name());
        apiException.setMessage(String.format(GE_415.getDescription(), ex.getContentType()));
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(apiException);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApplicationApiException> handleGenericException(Exception ex) {
        log.error("Unhandled Exception: {}", ex.getMessage(), ex);
        ApplicationApiException apiException = new ApplicationApiException();
        apiException.setCode(GE_500.name());
        apiException.setMessage(String.format(GE_500.getDescription(), ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiException);
    }
}