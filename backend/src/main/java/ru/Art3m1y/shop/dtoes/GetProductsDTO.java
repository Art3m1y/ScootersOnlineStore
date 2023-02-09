package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Schema(name = "Модель передачи данных для получения ответа при запросе списка продуктов", description = "Используется для удаления не нужных полей из основной модели Product")
@Setter
@Getter
public class GetProductsDTO {
    private long amount;
    private List<GetProductDTO> products;

    public GetProductsDTO(List<GetProductDTO> products) {
        this.products = products;
    }
}
