package ru.Art3m1y.shop.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.RestorePassword;
import ru.Art3m1y.shop.repositories.PersonRepository;
import ru.Art3m1y.shop.repositories.RestorePasswordRepository;
import ru.Art3m1y.shop.services.MailSenderService;
import ru.Art3m1y.shop.services.PersonService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;
    @Mock
    private MailSenderService mailSenderService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RestorePasswordRepository restorePasswordRepository;
    @InjectMocks
    private PersonService personService;

    @Test
    void existByEmail_shouldReturnBooleanValueOfExistPersonById_whenCalled() {
        String email = "artem.kuryshkin@gmail.com";
        when(personRepository.existsByEmail(email)).thenReturn(true);

        boolean isExist = personService.existByEmail(email);

        verify(personRepository, times(1)).existsByEmail(email);
        assertTrue(isExist);
    }

    @Test
    void save_shouldSavePerson_whenUserCreateAnAccount() {
        Person person = mock(Person.class);
        when(person.getEmail()).thenReturn("artem.kuryshkin@gmail.com");
        when(person.getPassword()).thenReturn("12345");
        when(person.getName()).thenReturn("Artem");
        when(person.getSurname()).thenReturn("Kuryshkin");
        when(passwordEncoder.encode(any())).thenReturn("encoded password");

        personService.save(person);

        verify(person, times(1)).setRole(eq("ROLE_USER"));
        verify(person, times(1)).setCreatedAt(any());
        verify(person, times(1)).setUpdatedAt(any());
        verify(person, times(1)).setActivationCode(any());
        verify(person, times(1)).getPassword();
        verify(person, times(1)).getName();
        verify(person, times(1)).getSurname();
        verify(personRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(person.getPassword());
        verify(mailSenderService, times(1)).send(eq(person.getEmail()), any(), any());
    }

    @Test
    void activate_shouldActivateUserAccount_whenUserClickOnActivateLink() {
        String code = "code";
        Person person = mock(Person.class);
        when(personRepository.findByActivationCode(code)).thenReturn(Optional.of(person));

        personService.activate(code);

        verify(personRepository, times(1)).findByActivationCode(code);
        verify(person, times(1)).setActivationCode(null);
    }

    @Test
    void restorePassword_shouldStartProcessOfRestoringPassword_whenUserRestoresPassword() {
        String email = "artem.kuryshkin@gmail.com";
        Person person = mock(Person.class);
        when(personRepository.findByEmail(email)).thenReturn(Optional.of(person));
        when(restorePasswordRepository.existsByPerson(person)).thenReturn(true);

        personService.restorePassword(email);

        verify(personRepository, times(1)).findByEmail(email);
        verify(restorePasswordRepository, times(1)).existsByPerson(person);
        verify(restorePasswordRepository, times(1)).deleteByPerson(person);
        verify(restorePasswordRepository, times(1)).save(any());
        verify(mailSenderService, times(1)).send(eq("artem.kuryshkin@gmail.com"), any(), any());
    }

    @Test
    void changePassword_shouldChangePasswordFromOldToNew_whenUserClickOnRestorePasswordLink() {
        String token = "token";
        String password = "12345";
        RestorePassword restorePassword = mock(RestorePassword.class);
        Person person = mock(Person.class);
        when(restorePassword.getPerson()).thenReturn(person);
        when(restorePasswordRepository.findByToken(token)).thenReturn(Optional.of(restorePassword));

        personService.changePassword(token, password, password);

        verify(restorePasswordRepository, times(1)).findByToken(token);
        verify(restorePassword, times(1)).getPerson();
        verify(person, times(1)).setPassword(any());
        verify(passwordEncoder, times(1)).encode("12345");
    }
}