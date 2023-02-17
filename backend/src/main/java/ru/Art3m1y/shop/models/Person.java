package ru.Art3m1y.shop.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;


@Schema(name = "Модель пользователя")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @EqualsAndHashCode.Include
    private long id;
    @Column
    @NotEmpty(message = "Поле с именем не может быть пустым.")
    private String name;
    @Column
    @NotEmpty(message = "Поле с фамилией не может быть пустым.")
    private String surname;
    @Column(name = "year_of_birth")
    @Min(value = 1900, message = "Год рождения не может быть менее, чем 1900 год.")
    @Max(value = 2022, message = "Год рождения не может быть более, чем 2022 год.")
    private int yearOfBirth;
    @Column
    @NotEmpty(message = "Поле со страной проживания не может быть пустым.")
    private String country;
    @Column
    @NotEmpty(message = "Почта не может быть пустой.")
    @Email(message = "Введите почту в правильном формате.")
    private String email;
    @Column
    @NotEmpty(message = "Почта не может быть пустой.")
    @Size(min = 5, message = "Минимальная длина пароля составляет 5 символов.")
    private String password;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column
    private String role;
    private String activationCode;
    @OneToOne(mappedBy = "person")
    private RefreshToken refreshToken;
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comment;
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart> cart;
    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private RestorePassword RestorePassword;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Person(long id) {
        this.id = id;
    }
}
