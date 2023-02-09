package ru.Art3m1y.shop.utils.exceptions;

public class AddProductToCartException extends RuntimeException {
    public AddProductToCartException(String message) {
        super(message);
    }
}
