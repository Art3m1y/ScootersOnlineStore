package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

    @Schema(name = "Модель передачи данных для получения комментария", description = "Используется для удаления не нужных полей из основной модели Comment")
@Getter
@Setter
public class GetCommentDTO {
    private long id;
    private PersonDTO person;
    private String text;
    private short mark;
}
