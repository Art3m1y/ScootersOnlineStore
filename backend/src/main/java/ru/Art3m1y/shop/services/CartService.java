package ru.Art3m1y.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public void addProductToCart(long productId, long amount, Person person) {
        Optional<Cart> optionalCart = getByProductAndPerson(new Product(productId), person);

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

    public Optional<Cart> getByProductAndPerson(Product product, Person person) {
        return cartRepository.findFirstByProductAndPerson(product, person);
    }


    public List<Cart> getProductFromCart(Person person) {
        return cartRepository.findAllByPerson(person);
    }

    public void deleteProductFromCart(long productId, boolean isAll, Person person) {
        Optional<Cart> optionalCart = getByProductAndPerson(new Product(productId), person);

        if (optionalCart.isEmpty()) {
            throw new RuntimeException("Не удалось найти запись в базе данных о таком продукте заданного пользователя");
        }

        Cart cart = optionalCart.get();

        if (isAll) {
            cartRepository.delete(cart);
        } else {
            cart.setAmount(cart.getAmount() - 1);

            cartRepository.save(cart);
        }
    }
}
