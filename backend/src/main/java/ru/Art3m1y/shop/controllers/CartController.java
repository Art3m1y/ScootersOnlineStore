package ru.Art3m1y.shop.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Art3m1y.shop.dtoes.AddProductToCartDTO;
import ru.Art3m1y.shop.dtoes.DeleteProductFromCartDTO;
import ru.Art3m1y.shop.dtoes.GetCartDTO;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.security.PersonDetails;
import ru.Art3m1y.shop.services.CartService;

import java.util.ArrayList;
import java.util.List;

import static ru.Art3m1y.shop.controllers.Helpers.validateRequestBody;

@Tag(name = "Корзина покупок")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Добавление продукта в корзину пользователя")
    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addProductToCart(@Valid @RequestBody AddProductToCartDTO addProductToCartDTO, BindingResult bindingResult) {
        validateRequestBody(bindingResult);

        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        cartService.addProductToCart(addProductToCartDTO.productId, addProductToCartDTO.amount, person);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение корзины пользователя")
    @GetMapping("/get")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<GetCartDTO>> getProductFromCart() {
        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        List<GetCartDTO> products = new ArrayList<>();

        cartService.getProductFromCart(person).forEach(product -> products.add(modelMapper.map(product, GetCartDTO.class)));

        return ResponseEntity.ok().body(products);
    }

    @Operation(summary = "Удаление одного продукта из корзины (одной штуки или полностью)", description = "Если флаг isAll положителен, то продукт удаляется полностью, иначе удаляется лишь одна штука")
    @DeleteMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteProductFromCart(@Valid @RequestBody DeleteProductFromCartDTO deleteProductFromCartDTO, BindingResult bindingResult) {
        validateRequestBody(bindingResult);

        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        cartService.deleteProductFromCart(deleteProductFromCartDTO.getProductId(), deleteProductFromCartDTO.isAll(), person);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Очистка корзины пользователя")
    @DeleteMapping("/deleteCart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteProductFromCart() {
        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        cartService.deleteAllProductsFromCart(person);

        return ResponseEntity.ok().build();
    }
}
