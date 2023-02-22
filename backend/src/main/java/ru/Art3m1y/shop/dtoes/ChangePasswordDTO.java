package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Модель передачи данных для смены пароля", description = "Используется для удаления не нужных полей из основной модели Person")
@Getter
@Setter
public class ChangePasswordDTO {
    @NotEmpty(message = "Пароль не может быть пустым")
    @Size(min = 5, message = "Минимальная длина пароля составляет 5 символов.")
    private String password;
    @NotEmpty(message = "Повторный пароль не может быть пустым")
    @Size(min = 5, message = "Минимальная длина повторного пароля составляет 5 символов.")
    private String confirmingPassword;
}
