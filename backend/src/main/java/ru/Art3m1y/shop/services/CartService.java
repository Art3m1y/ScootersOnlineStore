package ru.Art3m1y.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Art3m1y.shop.models.Cart;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.Product;
import ru.Art3m1y.shop.repositories.CartRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    @Transactional
    public void addProductToCart(long productId, long amount, Person person) {
        Optional<Cart> optionalCart = cartRepository.findFirstByProductAndPerson(new Product(productId), person);

        Cart cart;

        if (optionalCart.isPresent()) {
            cart = optionalCart.get();

            cart.setAmount(cart.getAmount() + amount);

            cartRepository.save(cart);
        } else {
            cart = new Cart(new Product(productId), amount, person);

            cartRepository.save(cart);
        }
    }

    @Transactional
    public Cart getByProductAndPerson(Product product, Person person) {
        return cartRepository.findFirstByProductAndPerson(product, person).orElseThrow(() -> new RuntimeException("Не удалось найти запись в базе данных о таком продукте заданного пользователя"));
    }

    @Transactional
    public List<Cart> getProductFromCart(Person person) {
        return cartRepository.findAllByPerson(person, Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional
    public void deleteProductFromCart(long productId, boolean isAll, Person person) {
        Cart cart = getByProductAndPerson(new Product(productId), person);

        if (isAll || cart.getAmount() == 1) {
            cartRepository.delete(cart);
        } else {
            cart.setAmount(cart.getAmount() - 1);

            cartRepository.save(cart);
        }
    }

    @Transactional
    public void deleteAllProductsFromCart(Person person) {
        cartRepository.deleteAllByPerson(person);
    }
}
