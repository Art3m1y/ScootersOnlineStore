package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Модель передачи данных для передачи продукта", description = "Используется для удаления не нужных полей из основной модели Product")
@Getter
@Setter
public class ProductDTO {
    @Min(value = 1,message = "Минимальное значение идентификатора равно 1")
    private long id;
}
