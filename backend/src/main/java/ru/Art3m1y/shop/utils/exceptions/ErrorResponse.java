package ru.Art3m1y.shop.utils.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Схема ответа при возникновении ошибок")
@Setter
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private long timestamp;
}
