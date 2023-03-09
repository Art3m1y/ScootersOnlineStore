package ru.Art3m1y.shop.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Art3m1y.shop.models.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByNameContainsIgnoreCase(Pageable pageable, String search);
    List<Product> findAllByNameContainsIgnoreCase(String search);
    long countAllByNameContainsIgnoreCase(String search);
}
