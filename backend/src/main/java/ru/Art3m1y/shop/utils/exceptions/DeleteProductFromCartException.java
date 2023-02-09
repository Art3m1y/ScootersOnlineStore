package ru.Art3m1y.shop.utils.exceptions;

public class DeleteProductFromCartException extends RuntimeException {
    public DeleteProductFromCartException(String message) {
        super(message);
    }
}
