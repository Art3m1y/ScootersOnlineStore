package ru.Art3m1y.shop.utils.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.services.RegistrationService;

@Component
public class RegistrationValidator implements Validator {
    private final RegistrationService registrationService;

    public RegistrationValidator(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (registrationService.findByName(person.getEmail())) {
            errors.rejectValue("email", "", "Почта уже используется.");
        }
    }
}
