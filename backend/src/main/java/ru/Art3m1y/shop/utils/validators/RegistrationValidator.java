package ru.Art3m1y.shop.utils.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.services.PersonService;

@Component
@RequiredArgsConstructor
public class RegistrationValidator implements Validator {
    private final PersonService personService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (personService.existByEmail(person.getEmail())) {
            errors.rejectValue("email", "", "Почта уже используется.");
        }
    }
}
