package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Модель передачи данных для регистрации", description = "Используется для удаления не нужных полей из основной модели Person")
@Setter
@Getter
public class RegistrationPersonDTO {
    @NotEmpty(message = "Поле с именем не может быть пустым.")
    private String name;
    @NotEmpty(message = "Поле с фамилией не может быть пустым.")
    private String surname;
    @Min(value = 1900, message = "Год рождения не может быть менее, чем 1900 год.")
    @Max(value = 2022, message = "Год рождения не может быть более, чем 2022 год.")
    private int yearOfBirth;
    @NotEmpty(message = "Поле со страной проживания не может быть пустым.")
    private String country;
    @NotEmpty(message = "Почта не может быть пустой.")
    @Email(message = "Введите почту в правильном формате.")
    private String email;
    @NotEmpty(message = "Почта не может быть пустой.")
    @Size(min = 5, message = "Минимальная длина пароля составляет 5 символов.")
    private String password;
}
