package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Модель передачи данных для добавления продукта в корзину", description = "Используется для удаления не нужных полей из основной модели Cart")
@Getter
@Setter
public class AddProductToCartDTO {
    @Min(value = 1,message = "Минимальное значение идентификатора равно 1")
    public long productId;
    @Max(value = 999,message = "Максимальное значение количества равно 999")
    public long amount;

}
