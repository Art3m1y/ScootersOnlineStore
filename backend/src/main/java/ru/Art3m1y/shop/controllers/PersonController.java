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
import org.springframework.web.multipart.MultipartFile;
import ru.Art3m1y.shop.dtoes.UpdatePersonFromPersonDTO;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.security.PersonDetails;
import ru.Art3m1y.shop.services.PersonService;
import ru.Art3m1y.shop.utils.validators.PersonValidator;

import static ru.Art3m1y.shop.controllers.Helpers.validateRequestBody;

@Tag(name = "Контроллер пользователя")
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PersonController {
    private final PersonService personService;
    private final ModelMapper modelMapper;
    private final PersonValidator personValidator;

    @Operation(summary = "Обновление информации о пользователе (включая аватар)")
    @PatchMapping("/update")
    public ResponseEntity<?> updatePerson(@RequestPart("person") @Valid UpdatePersonFromPersonDTO updatePersonFromPersonDTO, BindingResult bindingResult, @RequestPart(required = false) MultipartFile avatar) {
        Person personAuthenticated = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        Person person = modelMapper.map(updatePersonFromPersonDTO, Person.class);

        if ((person.getEmail() != null) && (!person.getEmail().equals(personAuthenticated.getEmail()))) {
            personValidator.validate(person, bindingResult);
        }

        validateRequestBody(bindingResult);

        person.setId(personAuthenticated.getId());

        personService.updatePerson(person, avatar);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление профиля пользователя по его идентификатору")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePerson() {
        Person personAuthenticated = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        personService.deletePersonById(personAuthenticated.getId());

        return ResponseEntity.ok().build();
    }
}
