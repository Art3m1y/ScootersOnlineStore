package ru.Art3m1y.shop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.Art3m1y.shop.utils.exceptions.ErrorResponse;

@RestControllerAdvice
public class RestExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(RuntimeException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(HttpMessageNotReadableException e) {
        ErrorResponse response = new ErrorResponse("Не удалось десереализировать переданные json-данные, ошибка: " + e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(AccessDeniedException e) {
        ErrorResponse response = new ErrorResponse("У вас нет прав для доступа к этому эндпоинту.", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(IllegalArgumentException e) {
        ErrorResponse response = new ErrorResponse("Неправильный аргумент при запросе, ошибка: " + e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
