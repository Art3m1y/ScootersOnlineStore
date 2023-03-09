package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Модель передачи данных для обновления информации о пользователе со стороны админ-панели", description = "Используется для удаления не нужных полей из основной модели Person")
@Getter
@Setter
public class UpdatePersonFromAdminDTO {
    @Min(value = 1, message = "Минимальное значение идентификатора равно одному")
    private long id;
    @Size(min = 3, max = 30, message = "Минимальная длина имени - 3, максимальная - 30")
    @NotEmpty(message = "Поле с именем не может быть пустым.")
    private String name;
    @Size(min = 3, max = 30, message = "Минимальная длина фамилии - 3, максимальная - 30")
    @NotEmpty(message = "Поле с фамилией не может быть пустым.")
    private String surname;
    @Min(value = 1900, message = "Год рождения не может быть менее, чем 1900 год.")
    @Max(value = 2022, message = "Год рождения не может быть более, чем 2022 год.")
    private int yearOfBirth;
    @Size(min = 3, max = 25, message = "Минимальная длина названия страны - 3, максимальная - 25")
    @NotEmpty(message = "Поле со страной проживания не может быть пустым.")
    private String country;
    @Size(min = 3, max = 50, message = "Минимальная длина почты - 3, максимальная - 50")
    @NotEmpty(message = "Почта не может быть пустой.")
    @Email(message = "Введите почту в правильном формате.")
    private String email;
    @Size(min = 5, message = "Минимальная длина пароля составляет 5 символов.")
    private String password;
    @NotEmpty(message = "Роль пользователя не может быть пустой")
    private String role;

}
