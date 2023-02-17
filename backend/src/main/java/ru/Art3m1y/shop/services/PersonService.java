package ru.Art3m1y.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.RestorePassword;
import ru.Art3m1y.shop.repositories.PersonRepository;
import ru.Art3m1y.shop.repositories.RefreshTokenRepository;
import ru.Art3m1y.shop.repositories.RestorePasswordRepository;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    private final RestorePasswordRepository restorePasswordRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public boolean existByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    @Transactional
    public void save(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        person.setCreatedAt(new Date());
        person.setUpdatedAt(new Date());

        String activationCode = UUID.randomUUID().toString();

        person.setActivationCode(activationCode);

        sendMailMessageToVerifyAccount(person.getName(), person.getSurname(), person.getEmail(), activationCode);

        personRepository.save(person);
    }

    @Transactional
    public void activate(String activationCode) {
        Person person = personRepository.findByActivationCode(activationCode).orElseThrow(() -> new RuntimeException("Не удалось активировать аккаунт, поскольку код активации является недействительным"));

        person.setActivationCode(null);

        personRepository.save(person);
    }

    @Transactional
    public void restorePassword(String email) {
        Person person = personRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Пользователь с такой почтой не найден"));

        if (restorePasswordRepository.existsByPerson(person)) {
            restorePasswordRepository.deleteByPerson(person);
        }

        RestorePassword restorePassword = new RestorePassword();

        String restoreToken = UUID.randomUUID().toString();

        restorePassword.setToken(restoreToken);
        restorePassword.setPerson(person);
        restorePassword.setCreatedAt(new Date());

        restorePasswordRepository.save(restorePassword);

        sendMailMessageToRestorePassword(person.getName(), person.getSurname(), email, restoreToken);
    }

    @Transactional
    public void changePassword(String restoreToken, String password, String confirmingPassword) {
        if (!password.equals(confirmingPassword)) {
            throw new RuntimeException("Введеные пароли не совпадают");
        }

        RestorePassword restorePassword = restorePasswordRepository.findByToken(restoreToken).orElseThrow(() -> new RuntimeException("Токен восстановления пароля является недействительным"));

        Person person = restorePassword.getPerson();

        person.setPassword(passwordEncoder.encode(password));

        personRepository.save(person);
    }

    private void sendMailMessageToVerifyAccount(String name, String surname, String email, String activationCode) {
        String domain = "https://shop.javaspringbackend.software/";

        String activationLink = domain + "/auth/activateAccount?code=" + activationCode;

        String subject = "Активация аккаунта";

        String message = String.format("Здравствуйте, %s %s! Пожалуйста, посетите следующую ссылку, чтобы подтвердить Ваш аккаунт: %s!", name, surname, activationLink);

        mailSenderService.send(email, subject, message);
    }

    private void sendMailMessageToRestorePassword(String name, String surname, String email, String restoreToken) {
        String domain = "https://shop.javaspringbackend.software/";

        String restorePasswordLink = domain + "/auth/restorePassword?token=" + restoreToken;

        String subject = "Восстановление пароля";

        String message = String.format("Здравствуйте, %s %s! Пожалуйста, посетите следующую ссылку, чтобы восстановить Ваш пароль: %s! Если запрос на восстановление пароля отправили не Вы, то просто проигнорируйте это сообщение!", name, surname, restorePasswordLink);

        mailSenderService.send(email, subject, message);
    }
}
