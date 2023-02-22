package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Модель передачи данных для добавления комментариев", description = "Используется для удаления не нужных полей из основной модели Comment")
@Getter
@Setter
public class AddCommentDTO {
    @Min(value = 1, message = "Минимальное значение идентификатора равно 1")
    private long id;
    @Min(value = 1, message = "Минимальное значение оценки равно 1")
    @Max(value = 5, message = "Максимальное значение оценки равно 5")
    private short mark;
    @NotBlank(message = "Текст комментария не может быть пустым")
    @Size(min = 1, max = 400, message = "Минимальная длина комментария равна 1 символу, максимальная - 400 символам")
    private String text;
}
