package ru.Art3m1y.shop.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Art3m1y.shop.dtoes.AddProductToCartDTO;
import ru.Art3m1y.shop.dtoes.DeleteProductFromCartDTO;
import ru.Art3m1y.shop.dtoes.GetCartDTO;
import ru.Art3m1y.shop.modelMappers.CartModelMapper;
import ru.Art3m1y.shop.models.Cart;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.Product;
import ru.Art3m1y.shop.repositories.CartRepository;
import ru.Art3m1y.shop.security.PersonDetails;
import ru.Art3m1y.shop.services.CartService;
import ru.Art3m1y.shop.utils.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CartModelMapper cartModelMapper;

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addProductToCart(@Valid @RequestBody AddProductToCartDTO addProductToCartDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorsMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> errorsMessage.append(error.getDefaultMessage()).append(";"));
            throw new AddProductToCartException(errorsMessage.toString());
        }

        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        cartService.addProductToCart(addProductToCartDTO.productId, addProductToCartDTO.amount, person);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<GetCartDTO>> getProductFromCart() {
        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        List<GetCartDTO> products = new ArrayList<>();

        cartService.getProductFromCart(person).forEach(product -> products.add(cartModelMapper.MapToGetCartDTO(product)));

        return ResponseEntity.ok().body(products);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteProductFromCart(@Valid @RequestBody DeleteProductFromCartDTO deleteProductFromCartDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorsMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> errorsMessage.append(error.getDefaultMessage()).append(";"));
            throw new DeleteProductFromCartException(errorsMessage.toString());
        }

        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        cartService.deleteProductFromCart(deleteProductFromCartDTO.getProductId(), deleteProductFromCartDTO.isAll(), person);

        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({AddProductToCartException.class, DeleteProductFromCartException.class})
    private ResponseEntity<ErrorResponse> handlerException(RuntimeException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlerException(HttpMessageNotReadableException e) {
        ErrorResponse response = new ErrorResponse("Не удалось десереализировать переданные json-данные, ошибка: " + e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
