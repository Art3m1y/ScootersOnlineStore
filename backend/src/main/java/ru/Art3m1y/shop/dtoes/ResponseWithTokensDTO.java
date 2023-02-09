package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Модель передачи данных для получения ответа при регистрации и регистрации", description = "Используется для удаления не нужных полей из основной модели RefreshToken")
@Setter
@Getter
@AllArgsConstructor
public class ResponseWithTokensDTO {
    private String accessToken;
    private String refreshToken;

    public ResponseWithTokensDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
