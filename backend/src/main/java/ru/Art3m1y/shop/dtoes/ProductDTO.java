package ru.Art3m1y.shop.dtoes;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    @Min(value = 1,message = "Минимальное значение идентификатора равно 1")
    private long id;
}
