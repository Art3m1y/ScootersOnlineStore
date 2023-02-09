package ru.Art3m1y.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.repositories.OrderRepository;
import ru.Art3m1y.shop.security.PersonDetails;
import ru.Art3m1y.shop.services.OrderService;
import ru.Art3m1y.shop.utils.exceptions.ErrorResponse;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> makeOrder() {
        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        orderService.makeOrder(person);

        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(HttpMessageNotReadableException e) {
        ErrorResponse response = new ErrorResponse("Не удалось десереализировать переданные json-данные, ошибка: " + e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
