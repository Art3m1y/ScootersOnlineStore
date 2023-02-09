package ru.Art3m1y.shop.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
