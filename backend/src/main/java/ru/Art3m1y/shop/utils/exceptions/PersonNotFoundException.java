package ru.Art3m1y.shop.utils.exceptions;

public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException() {
        super("Пользователь с таким идентификатором не найден");
    }
}
