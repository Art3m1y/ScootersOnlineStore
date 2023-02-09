package ru.Art3m1y.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Art3m1y.shop.models.Image;
import ru.Art3m1y.shop.models.Product;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByProduct(Product product);
}
