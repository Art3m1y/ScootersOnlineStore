package ru.Art3m1y.shop.controllers;

import org.springframework.validation.BindingResult;

public class Helpers {
    public static void validateRequestBody(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> errors.append(error.getDefaultMessage()).append("; "));
            throw new RuntimeException(errors.toString());
        }
    }

    public static void checkConvertFromStringToInteger(String number) {
        try {
            int number_converted = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Не удалось преобразовать стровку в интеджер-формат");
        }
    }
}
