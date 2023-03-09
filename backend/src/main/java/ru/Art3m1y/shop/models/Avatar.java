package ru.Art3m1y.shop.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.Art3m1y.shop.utils.enums.ContentType;

import java.util.Date;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private Person person;
    @Enumerated(EnumType.ORDINAL)
    private ContentType contentType;
    private String originalFileName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Transient
    private String link;

    public Avatar(ContentType contentType, String originalFileName) {
        this.contentType = contentType;
        this.originalFileName = originalFileName;
    }

    public String getLink() {
        return "https://shop.javaspringbackend.software" + "/avatar/" + id;
    }
}
