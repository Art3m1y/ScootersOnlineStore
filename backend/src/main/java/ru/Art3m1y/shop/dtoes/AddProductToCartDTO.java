package ru.Art3m1y.shop.dtoes;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductToCartDTO {
    @Min(value = 1,message = "Минимальное значение идентификатора равно 1")
    public long productId;
    @Max(value = 999,message = "Максимальное значение количества равно 999")
    public long amount;

}
