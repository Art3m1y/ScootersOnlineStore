package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Модель передачи данных для получения корзины", description = "Используется для удаления не нужных полей из основной модели Cart")
@Getter
@Setter
public class GetCartDTO {
    private GetProductDTO product;
    private long amount;
}
