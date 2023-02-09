package ru.Art3m1y.shop.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.repositories.PersonRepository;

import java.util.Date;

@Service
public class RegistrationService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public boolean findByName(String email) {
        return personRepository.findByEmail(email).isPresent() ? true : false;
    }

    @Transactional()
    public void save(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        person.setCreatedAt(new Date());
        person.setUpdatedAt(new Date());
        personRepository.save(person);
    }
}
