package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Модель передачи данных для передачи пользователя", description = "Используется для удаления не нужных полей из основной модели Person")
@Getter
@Setter
public class PersonDTO {
    private long id;
    private String name;
    private String surname;
}
