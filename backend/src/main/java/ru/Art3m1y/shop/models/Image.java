package ru.Art3m1y.shop.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.Art3m1y.shop.utils.enums.ContentType;

import java.util.Date;

@Schema(name = "Модель изображения для продукта")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    @Enumerated(EnumType.ORDINAL)
    private ContentType contentType;
    private String originalFileName;
    @Transient
    private String link;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    public Image(ContentType contentType, String originalFileName) {
        this.contentType = contentType;
        this.originalFileName = originalFileName;
    }

    public String getLink() {
        return "https://shop.javaspringbackend.software" + "/image/" + id;
    }
}
