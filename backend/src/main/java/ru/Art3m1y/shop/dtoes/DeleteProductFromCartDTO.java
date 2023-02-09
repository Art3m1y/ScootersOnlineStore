package ru.Art3m1y.shop.dtoes;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteProductFromCartDTO {
    @Min(value = 1,message = "Минимальное значение идентификатора равно 1")
    public long productId;
    public boolean isAll;
}
