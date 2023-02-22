package ru.Art3m1y.shop.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Schema(name = "Модель рефреш токена")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @EqualsAndHashCode.Include
    private long id;
    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    public RefreshToken(Person person) {
        this.person = person;
    }

    public RefreshToken(long id) {
        this.id = id;
    }
}

