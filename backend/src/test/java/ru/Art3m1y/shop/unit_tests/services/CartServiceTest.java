package ru.Art3m1y.shop.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.Art3m1y.shop.models.Cart;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.Product;
import ru.Art3m1y.shop.repositories.CartRepository;
import ru.Art3m1y.shop.services.CartService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @InjectMocks
    private CartService cartService;

    @Test
    void addProductToCart_shouldAddProductToPersonCart_whenUserAddProductToCart_exists() {
        Cart cart = mock(Cart.class);
        Person person = new Person(1);
        Product product = new Product(1);
        when(cart.getAmount()).thenReturn(10L);
        when(cartRepository.findFirstByProductAndPerson(product, person)).thenReturn(Optional.ofNullable(cart));

        cartService.addProductToCart(1, 30, person);

        verify(cartRepository, times(1)).findFirstByProductAndPerson(product, person);
        verify(cart, times(1)).setAmount(10 + 30);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void addProductToCart_shouldAddProductToPersonCart_whenUserAddProductToCart_doesNotExist() {
        Person person = new Person(1);
        Product product = new Product(1);
        Cart cart = new Cart();
        when(cartRepository.findFirstByProductAndPerson(product, person)).thenReturn(Optional.empty());

        cartService.addProductToCart(1, 30, person);

        verify(cartRepository, times(1)).findFirstByProductAndPerson(product, person);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void getByProductAndPerson_shouldGetProductFromCartByProductAndPerson_whenCalled_exists() {
        Product product = new Product();
        Person person = new Person();
        Cart cart = new Cart();
        cart.setProduct(product);
        cart.setPerson(person);
        when(cartRepository.findFirstByProductAndPerson(product, person)).thenReturn(Optional.ofNullable(cart));

        Cart cartResult =  cartService.getByProductAndPerson(product, person);

        verify(cartRepository, times(1)).findFirstByProductAndPerson(product, person);
        assertNotNull(cartResult);
        assertEquals(product, cartResult.getProduct());
        assertEquals(person, cartResult.getPerson());
    }

    @Test
    void getByProductAndPerson_shouldGetProductFromCartByProductAndPerson_whenCalled_doesNotExist_shouldThrowRuntimeException() {
        Product product = new Product();
        Person person = new Person();
        when(cartRepository.findFirstByProductAndPerson(product, person)).thenReturn(Optional.empty());

        try {
            Cart cart = cartService.getByProductAndPerson(product, person);
            fail("Ожидалось выбрасывание RuntimeException");
        } catch (RuntimeException ignored) {
        }

        verify(cartRepository, times(1)).findFirstByProductAndPerson(product, person);
    }

    @Test
    void getProductFromCart_shouldGetAllProductsFromCartByPerson_whenUserRequestsCart() {
        Person person = new Person();
        List<Cart> carts = Arrays.asList(new Cart(1), new Cart(2));

        when(cartRepository.findAllByPerson(person, Sort.by(Sort.Direction.ASC, "id"))).thenReturn(carts);

        List<Cart> cartsResult = cartService.getProductFromCart(person);

        verify(cartRepository, times(1)).findAllByPerson(person, Sort.by(Sort.Direction.ASC, "id"));
        assertNotNull(cartsResult);
        assertEquals(carts.size(), cartsResult.size());
        assertEquals(carts.get(0), cartsResult.get(0));
        assertEquals(carts.get(1), cartsResult.get(1));
    }

    @Test
    void deleteProductFromCart_shouldDeleteOneProductFromPersonCartByPersonAndStatus_whenUserWantToDeleteProductFromCart_cartExists_isAllTrue() {
        Cart cart = new Cart();
        Person person = new Person(1);
        Product product = new Product(1);
        when(cartRepository.findFirstByProductAndPerson(product, person)).thenReturn(Optional.ofNullable(cart));

        cartService.deleteProductFromCart(1, true, person);

        verify(cartRepository, times(1)).findFirstByProductAndPerson(product, person);
        verify(cartRepository, times(1)).delete(cart);
        verify(cartRepository, times(0)).save(cart);
    }

    @Test
    void deleteProductFromCart_shouldDeleteOneProductFromPersonCartByPersonAndStatus_whenUserWantToDeleteProductFromCart_cartExists_AmountEqualsOne() {
        Cart cart = new Cart();
        Person person = new Person(1);
        Product product = new Product(1);
        cart.setAmount(1);
        when(cartRepository.findFirstByProductAndPerson(product, person)).thenReturn(Optional.ofNullable(cart));

        cartService.deleteProductFromCart(1, false, person);

        verify(cartRepository, times(1)).findFirstByProductAndPerson(product, person);
        verify(cartRepository, times(1)).delete(cart);
        verify(cartRepository, times(0)).save(cart);
    }

    @Test
    void deleteProductFromCart_shouldDeleteOneProductFromPersonCartByPersonAndStatus_whenUserWantToDeleteProductFromCart_cartExists_AmountBiggerThanOne() {
        Cart cart = mock(Cart.class);
        Person person = new Person(1);
        Product product = new Product(1);
        when(cartRepository.findFirstByProductAndPerson(product, person)).thenReturn(Optional.ofNullable(cart));
        when(cart.getAmount()).thenReturn(10L);

        cartService.deleteProductFromCart(1, false, person);

        verify(cartRepository, times(1)).findFirstByProductAndPerson(product, person);
        verify(cartRepository, times(0)).delete(cart);
        verify(cartRepository, times(1)).save(cart);
        verify(cart, times(1)).setAmount(9);
    }


    @Test
    void deleteAllProductsFromCart_shouldDeleteAllProductsFromCartByPerson_whenUserMakeOrder() {
        Person person = new Person(1);

        cartService.deleteAllProductsFromCart(person);

        verify(cartRepository, times(1)).deleteAllByPerson(person);
    }
}