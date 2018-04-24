package com.trendyol.ecampman.campaign.api.controller;

import com.trendyol.ecampman.campaign.api.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<ErrorResponse> handleValidationError(ValidationException ex) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponse> handleGenericError(Exception ex) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse("Technical exception occurred!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
