package ru.Art3m1y.shop.dtoes;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetProductDTO2 {
    private long id;
    private String name;
    private int cost;
    private byte batteryCapacity;
    private float power;
    private byte speed;
    private byte time;
    private String description;
    private List<GetImageDTO> images;
    private float mark;
}
