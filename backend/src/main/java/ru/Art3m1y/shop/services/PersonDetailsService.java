package ru.Art3m1y.shop.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.repositories.PersonRepository;
import ru.Art3m1y.shop.security.PersonDetails;

import java.util.Optional;
@Service
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        Optional<Person> person = personRepository.findByEmail(email);

        if (person.isEmpty()) {
            throw new UsernameNotFoundException("Неверная почта пользователя или пароль");
        }

        return new PersonDetails(person.get());
    }
}
