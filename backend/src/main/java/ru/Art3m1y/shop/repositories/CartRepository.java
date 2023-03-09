package ru.Art3m1y.shop.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Art3m1y.shop.models.Cart;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByPerson(Person person, Sort sort);
    Optional<Cart> findFirstByProductAndPerson(Product product, Person person);
    void deleteAllByPerson(Person person);
}
