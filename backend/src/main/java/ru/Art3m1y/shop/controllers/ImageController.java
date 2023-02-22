package ru.Art3m1y.shop.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.Art3m1y.shop.services.ImageService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static ru.Art3m1y.shop.controllers.Helpers.checkConvertFromStringToLong;


@Tag(name = "Работа с изображениями для продуктов")
@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;
    @Operation(summary = "Получение изображение для продукта по идентификатору изображения")
    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> getImageById(@PathVariable String id) {
        checkConvertFromStringToLong(id);

        try {
            return ResponseEntity.ok()
                    .contentType(imageService.getMediaTypeByImageId(Long.parseLong(id)))
                    .body(new InputStreamResource(new FileInputStream(imageService.getImageById(Long.parseLong(id)))));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Не найдено физическое представление изображения с таким идентификатором (изображение найдено только в базе данных)");       }
    }

    //Для скрипта для очистки не нужных изображений
    @Operation(summary = "Получение имен всех изображений (Для скрипта для очистки ненужных изображений)")
    @GetMapping("/getAllImagesNames")
    public ResponseEntity<List<String>> getAllImagesNames() {
        return ResponseEntity.ok().body(imageService.getAllImagesNames());
    }
}
