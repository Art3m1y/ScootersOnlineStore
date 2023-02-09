package ru.Art3m1y.shop.dtoes;

import lombok.Getter;
import lombok.Setter;
import ru.Art3m1y.shop.models.Person;

@Getter
@Setter
public class GetCommentDTO {
    private long id;
    private PersonDTO person;
    private String text;
    private short mark;
}
