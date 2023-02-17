package ru.Art3m1y.shop.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Art3m1y.shop.services.ImageService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static ru.Art3m1y.shop.controllers.Helpers.checkConvertFromStringToInteger;


@Tag(name = "Работа с изображениями для продуктов")
@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;
    @Operation(summary = "Получение изображение для продукта по его идентификатора")
    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> getImageById(@PathVariable String id) {
        checkConvertFromStringToInteger(id);

        try {
            return ResponseEntity.ok()
                    .contentType(imageService.getMediaTypeByImageId(Long.parseLong(id)))
                    .body(new InputStreamResource(new FileInputStream(imageService.getImageById(Long.parseLong(id)))));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Не найдено физическое представление изображения с таким идентификатором (изображение найдено только в базе данных)");       }
    }
}
