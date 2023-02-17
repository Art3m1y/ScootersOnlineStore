package ru.Art3m1y.shop.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    private long amount;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    public Cart(Product product, long amount, Person person) {
        this.product = product;
        this.amount = amount;
        this.person = person;
    }

    public Cart(long id) {
        this.id = id;
    }
}
