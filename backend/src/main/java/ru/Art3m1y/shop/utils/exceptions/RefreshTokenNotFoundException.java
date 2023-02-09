package ru.Art3m1y.shop.utils.exceptions;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException() {
        super("Токен обновления с таким идентификатором не найден");
    }
}
