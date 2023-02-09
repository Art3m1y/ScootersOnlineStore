package ru.Art3m1y.shop.dtoes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import ru.Art3m1y.shop.models.Comment;
import ru.Art3m1y.shop.models.Image;

import java.util.List;

@Schema(name = "Модель передачи данных для получения ответа при запросе продукта", description = "Используется для удаления не нужных полей из основной модели Product")
@Getter
@Setter
public class GetProductDTO {
    private long id;
    private String name;
    private int cost;
    private byte batteryCapacity;
    private float power;
    private byte speed;
    private byte time;
    private String description;
    private List<GetImageDTO> images;
    private List<GetCommentDTO> comments;
    private float mark;

}
