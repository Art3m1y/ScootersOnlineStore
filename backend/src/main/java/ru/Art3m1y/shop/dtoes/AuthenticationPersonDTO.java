package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


@Schema(name = "Модель передачи данных для авторизации", description = "Используется для удаления не нужных полей из основной модели Person")
@Getter
@Setter
public class AuthenticationPersonDTO {
    @NotEmpty(message = "Почта не может быть пустой.")
    private String email;
    @NotEmpty(message = "Пароль не может быть пустой.")
    private String password;
}
