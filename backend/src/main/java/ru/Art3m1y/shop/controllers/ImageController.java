package ru.Art3m1y.shop.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Helper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.Art3m1y.shop.services.ImageService;
import ru.Art3m1y.shop.utils.exceptions.ErrorResponse;
import ru.Art3m1y.shop.utils.exceptions.GetImageException;
import ru.Art3m1y.shop.utils.exceptions.ProductSaveException;
import ru.Art3m1y.shop.utils.other.Helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


@Tag(name = "Работа с изображениями для продуктов")
@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;
    private final Helpers helpers;

    @Operation(summary = "Получение изображение для продукта по его идентификатора")
    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> getImageById(@PathVariable String id) {
        if (!helpers.checkConvertFromStringToLong(id)) {
            throw new GetImageException("Не удалось преобразовать строковое значение идентификатора изображения в лонг-формат");
        }

        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(new FileInputStream(imageService.getImageById(Long.parseLong(id)))));
        } catch (FileNotFoundException e) {
            throw new GetImageException("Не найдено физическое представление изображения с таким идентификатором (изображение найдено только в базе данных)");       }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GetImageException.class)
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
}
