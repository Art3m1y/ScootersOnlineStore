package ru.Art3m1y.shop.repositories;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Art3m1y.shop.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
