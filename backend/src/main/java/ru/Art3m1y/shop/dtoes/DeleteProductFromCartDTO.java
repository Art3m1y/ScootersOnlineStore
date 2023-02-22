package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Модель передачи данных для удаления продукта из корзины")
@Getter
@Setter
public class DeleteProductFromCartDTO {
    @Min(value = 1,message = "Минимальное значение идентификатора равно 1")
    public long productId;
    public boolean isAll;
}
