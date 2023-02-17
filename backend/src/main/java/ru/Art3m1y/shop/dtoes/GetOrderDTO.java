package ru.Art3m1y.shop.dtoes;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetOrderDTO {
    private long id;
    private PersonDTO person;
    private List<GetCartDTO> cart;
}
