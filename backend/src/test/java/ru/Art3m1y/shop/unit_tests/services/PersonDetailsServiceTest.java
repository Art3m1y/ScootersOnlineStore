package ru.Art3m1y.shop.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.repositories.PersonRepository;
import ru.Art3m1y.shop.security.PersonDetails;
import ru.Art3m1y.shop.services.PersonDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonDetailsServiceTest{
    @Mock
    private PersonRepository personRepository;
    @InjectMocks
    private PersonDetailsService personDetailsService;

    @Test
    void loadUserByUsername_shouldLoadUserProfileByEmail_whenUserLogins() {
        String email = "test@mail.ru";
        Person person = Mockito.mock(Person.class);
        when(personRepository.findByEmail(email)).thenReturn(Optional.ofNullable(person));

        UserDetails userProfile = personDetailsService.loadUserByUsername(email);

        verify(personRepository, times(1)).findByEmail(email);
        assertNotNull(userProfile);
        assertTrue(userProfile instanceof PersonDetails);
        assertEquals(person, ((PersonDetails) userProfile).getPerson());
    }

    @Test
    void loadUserByUsername_shouldLoadUserProfileByEmail_whenUserLogins_whenPersonDoesNotExist() throws RuntimeException {
        String email = "test@mail.ru";
        when(personRepository.findByEmail(email)).thenReturn(Optional.empty());

        try {
            UserDetails userProfile = personDetailsService.loadUserByUsername(email);
            fail("Ожидалось выбрасывание RuntimeException");
        } catch (RuntimeException ignored) {
        }

        verify(personRepository, times(1)).findByEmail(email);
    }
}