package ru.Art3m1y.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.RestorePassword;
import ru.Art3m1y.shop.repositories.PersonRepository;
import ru.Art3m1y.shop.repositories.RefreshTokenRepository;
import ru.Art3m1y.shop.repositories.RestorePasswordRepository;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonService {
    @Value("${frontend-url}")
    private String frontendUrl;
    @Value("${backend-url}")
    private String backendUrl;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    private final RestorePasswordRepository restorePasswordRepository;
    private final AvatarService avatarService;

    @Transactional(readOnly = true)
    public Person findById(long id) {
        return personRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь с таким идентификатором не найден"));
    }

    @Transactional(readOnly = true)
    public boolean existByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(Person person, MultipartFile avatar) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        person.setCreatedAt(new Date());
        person.setUpdatedAt(new Date());

        String activationCode = UUID.randomUUID().toString();

        person.setActivationCode(activationCode);

        personRepository.save(person);

        if ((avatar != null) && (avatar.getSize() != 0)) {
            avatarService.saveAvatar(person, avatar);
        }

        sendMailMessageToVerifyAccount(person.getName(), person.getSurname(), person.getEmail(), activationCode);
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

        long milliseconds_in_day = 86400000;
        if ((new Date().getTime()) - restorePassword.getCreatedAt().getTime() > milliseconds_in_day) {
            throw new RuntimeException("Срок действия токена восстановления пароля истек");
        }

        Person person = restorePassword.getPerson();

        person.setPassword(passwordEncoder.encode(password));

        personRepository.save(person);
    }

    @Transactional
    public void updatePerson(Person person, MultipartFile avatar) {
        Person personToUpdate = findById(person.getId());

        if (person.getPassword() != null) {
            personToUpdate.setPassword(passwordEncoder.encode(person.getPassword()));
        }

        personToUpdate.setEmail(person.getEmail());
        personToUpdate.setName(person.getName());
        personToUpdate.setSurname(person.getSurname());
        personToUpdate.setCountry(person.getCountry());
        personToUpdate.setYearOfBirth(person.getYearOfBirth());

        personToUpdate.setUpdatedAt(new Date());

        personRepository.save(personToUpdate);

        if ((avatar != null) && (avatar.getSize() != 0)) {
            avatarService.updateAvatar(personToUpdate, avatar);
        }
    }

    private void sendMailMessageToVerifyAccount(String name, String surname, String email, String activationCode) {
        String activationLink = frontendUrl + "verification/" + activationCode;

        String subject = "Активация аккаунта";

        String message = String.format("Здравствуйте, %s %s! Пожалуйста, посетите следующую ссылку, чтобы подтвердить Ваш аккаунт: %s!", name, surname, activationLink);

        mailSenderService.send(email, subject, message);
    }

    private void sendMailMessageToRestorePassword(String name, String surname, String email, String restoreToken) {
        String restorePasswordLink = backendUrl + "auth/restorePassword?token=" + restoreToken;

        String subject = "Восстановление пароля";

        String message = String.format("Здравствуйте, %s %s! Пожалуйста, посетите следующую ссылку, чтобы восстановить Ваш пароль: %s! Если запрос на восстановление пароля отправили не Вы, то просто проигнорируйте это сообщение!", name, surname, restorePasswordLink);

        mailSenderService.send(email, subject, message);
    }

    public void deletePersonById(long id) {
        if (!personRepository.existsById(id)) {
            throw new RuntimeException("Пользователя с таким идентификатором не существует");
        }

        personRepository.deleteById(id);
    }
}
