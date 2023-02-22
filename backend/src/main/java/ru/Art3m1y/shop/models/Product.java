package ru.Art3m1y.shop.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Schema(name = "Модель продукта")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    private String name;
    private int cost;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;
    @Transient
    private float mark;
    private byte batteryCapacity;
    private float power;
    private byte speed;
    private byte time;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart> cart;



    public Product(long id) {
        this.id = id;
    }

    public float getMark() {
        final int[] sum = {0};
        final int[] quantity = {0};

        comments.forEach(comment -> {
            sum[0] += comment.getMark();
            quantity[0]++;
        });

        if (quantity[0] == 0) {
            return 0;
        }

        return ((float) sum[0] / quantity[0]);
    }

    public void addImageToProduct(Image image) {
        if (images == null) {
            images = new ArrayList<>();
        }

        image.setProduct(this);

        images.add(image);
    }



}
