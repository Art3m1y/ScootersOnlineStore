package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Модель передачи данных для сохранения продукта", description = "Используется для удаления не нужных полей из основной модели Product")
@Getter
@Setter
public class SaveProductDTO {
    @NotEmpty(message = "Поле с именем продукта не может быть пустым")
    @Size(min = 1, max = 30, message = "Минимальная длина названия продукта - 1 символа, максимальная - 30 символов")
    private String name;
    @Min(value = 1, message = "Минимальное значение стоимости составляет 1 рубль")
    @Max(value = 9999999, message = "Максимальное значение стоимости составляет 9999999 рублей")
    private int cost;
    @NotEmpty(message = "Поле с описанием продукта не может быть пустым")
    @Size(min = 1, max = 400, message = "Минимальная длина описания - 1 символ, максимальная - 400 символ")
    private String description;
    @Min(value = 1, message = "Минимальное значение емкости батареи самоката равно 1 ампер")
    @Max(value = 50, message = "Максимальное значение емкости батареи самоката равно 50 ампер")
    private byte batteryCapacity;
    @Min(value = 1, message = "Минимальное значение мощности самоката равно 1 л/с")
    @Max(value = 25, message = "Максимальное значение мощности самоката равно 25 л/с")
    private float power;
    @Min(value = 1, message = "Минимальное значение скорости самоката равно 1 км/час")
    @Max(value = 100, message = "Максимальное значение скорости самоката равно 100 км/час")
    private byte speed;
    @Min(value = 1, message = "Минимальное значение времени работы самоката равно 1 часу")
    @Max(value = 30, message = "Максимальное значение времени работы самоката равно 30 часам")
    private byte time;
}
