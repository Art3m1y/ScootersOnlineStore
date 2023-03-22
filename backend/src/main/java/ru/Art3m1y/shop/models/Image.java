package ru.Art3m1y.shop.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import ru.Art3m1y.shop.utils.enums.ContentType;

import java.util.Date;

@Schema(name = "Модель изображения для продукта")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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
        return "https://scooters-shop.art3m1y.me/" + "image/" + id;
    }
}
