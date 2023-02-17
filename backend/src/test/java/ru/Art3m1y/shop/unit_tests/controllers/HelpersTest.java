package ru.Art3m1y.shop.unit_tests.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.Art3m1y.shop.controllers.Helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HelpersTest {

    @Test
    void validateRequestBody_shouldValidateBindingResult_whenCalled_hasErrors() {
        String errorMessage = "test_message";
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError error = mock(FieldError.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(error.getDefaultMessage()).thenReturn(errorMessage);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(error));

        try {
            Helpers.validateRequestBody(bindingResult);
            fail("Ожидалось выбрасывание RuntimeException");
        } catch (RuntimeException e) {
            assertEquals(errorMessage + "; ", e.getMessage());
        }
    }

    @Test
    void validateRequestBody_shouldValidateBindingResult_whenCalled_hasNotErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        Helpers.validateRequestBody(bindingResult);

        verify(bindingResult, times(1)).hasErrors();
    }

    @Test
    void checkConvertFromStringToInteger_shouldCheckCanBeStringConvertedToInteger_whenCalled_cantBeConverted() {
        String errorMessage = "Не удалось преобразовать стровку в интеджер-формат";
        String number = "10a";

        try {
            Helpers.checkConvertFromStringToInteger(number);
            fail("Ожидалось выбрасывание RuntimeException");
        } catch (RuntimeException e) {
            assertEquals(errorMessage, e.getMessage());
        }
    }
}