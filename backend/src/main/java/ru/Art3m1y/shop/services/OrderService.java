package ru.Art3m1y.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import ru.Art3m1y.shop.models.Order;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.repositories.CartRepository;
import ru.Art3m1y.shop.repositories.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public void makeOrder(Person person) {

    }
}
