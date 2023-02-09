package ru.Art3m1y.shop.dtoes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCartDTO {
    private GetProductDTO2 product;
    private long amount;
}
