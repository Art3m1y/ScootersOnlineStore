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
import ru.Art3m1y.shop.services.AvatarService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static ru.Art3m1y.shop.controllers.Helpers.checkConvertFromStringToLong;

@Tag(name = "Контроллер аватаров")
@RestController
@RequestMapping("/avatar")
@RequiredArgsConstructor
public class AvatarController {
    private final AvatarService avatarService;

    @Operation(summary = "Получить аватар пользователя по идентификатору пользователя")
    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> getAvatarById(@PathVariable("id") String id_string) {
        checkConvertFromStringToLong(id_string);

        long id = Long.parseLong(id_string);

        try {
            return ResponseEntity.ok()
                    .contentType(avatarService.getMediaTypeByPersonId(id))
                    .body(new InputStreamResource(new FileInputStream(avatarService.getImageByPersonId(id))));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Не найдено физическое представление изображения с таким идентификатором (изображение найдено только в базе данных)");       }
    }
}
