package com.example.restaurant.api.exceptions.advice;

import com.example.restaurant.api.exceptions.customExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceExceptions {

    @ExceptionHandler(BadDishNameException.class)
    public ResponseEntity<ErrorResponse> handleException(BadDishNameException e) {
        ErrorResponse response = new ErrorResponse();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadDishQuantityException.class)
    public ResponseEntity<ErrorResponse> handleException(BadDishQuantityException e) {
        ErrorResponse response = new ErrorResponse();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadDishPriceException.class)
    public ResponseEntity<ErrorResponse> handleException(BadDishPriceException e) {
        ErrorResponse response = new ErrorResponse();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadDishDifficultyException.class)
    public ResponseEntity<ErrorResponse> handleException(BadDishDifficultyException e) {
        ErrorResponse response = new ErrorResponse();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundDishException.class)
    public ResponseEntity<ErrorResponse> handleException(NotFoundDishException e) {
        ErrorResponse response = new ErrorResponse();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
