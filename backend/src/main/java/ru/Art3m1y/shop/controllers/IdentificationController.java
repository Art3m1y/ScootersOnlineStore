package ru.Art3m1y.shop.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Art3m1y.shop.dtoes.AuthenticationPersonDTO;
import ru.Art3m1y.shop.dtoes.RegistrationPersonDTO;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.RefreshToken;
import ru.Art3m1y.shop.security.PersonDetails;
import ru.Art3m1y.shop.services.PersonService;
import ru.Art3m1y.shop.services.RefreshTokenService;
import ru.Art3m1y.shop.utils.exceptions.*;
import ru.Art3m1y.shop.utils.jwt.JWTUtil;
import ru.Art3m1y.shop.utils.validators.RegistrationValidator;

import java.util.Map;
import java.util.Optional;

import static ru.Art3m1y.shop.controllers.Helpers.validateRequestBody;

@Tag(name = "Регистрация, Аутентификация, идентификация, авторизация пользователей")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class IdentificationController {
    private final PersonService personService;
    private final ModelMapper modelMapper;
    private final RegistrationValidator registrationValidator;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final int cookie_max_age = 259200;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/registration")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> registration(@RequestBody @Valid RegistrationPersonDTO registrationPersonDTO, BindingResult bindingResult, HttpServletResponse response) {
        Person person = modelMapper.map(registrationPersonDTO, Person.class);

        registrationValidator.validate(person, bindingResult);

        validateRequestBody(bindingResult);

        personService.save(person);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Аутентификация пользователя")
    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationPersonDTO authenticationPersonDTO, BindingResult bindingResult, HttpServletResponse response) {
        Person person = modelMapper.map(authenticationPersonDTO, Person.class);

        validateRequestBody(bindingResult);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(person.getEmail(), person.getPassword());

        Authentication auth = authenticationManager.authenticate(token);

        Person personAuthenticated = ((PersonDetails) auth.getPrincipal()).getPerson();

        if (personAuthenticated.getActivationCode() != null) {
            throw new RuntimeException("Аккаунт не подтвержден");
        }

        RefreshToken refreshToken = new RefreshToken(personAuthenticated);

        refreshTokenService.updateRefreshToken(personAuthenticated, refreshToken);

        return returnRefreshAndAccessTokens(response, refreshToken, personAuthenticated);
    }
    @Operation(summary = "Выход из аккаунта пользователя")
    @GetMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout(@CookieValue("refreshToken") Optional<String> refreshTokenFromCookie, HttpServletResponse response) {
        if (refreshTokenFromCookie.isPresent()) {
            String refreshToken = refreshTokenFromCookie.get();
            if (jwtUtil.verifyRefreshToken(refreshToken) && refreshTokenService.existsById(jwtUtil.getIdFromRefreshToken(refreshToken))) {
                Cookie deleteRefreshTokenCookie = new Cookie("refreshToken", null);
                deleteRefreshTokenCookie.setMaxAge(0);
                response.addCookie(deleteRefreshTokenCookie);
                refreshTokenService.deleteById(jwtUtil.getIdFromRefreshToken(refreshToken));
                return ResponseEntity.ok().build();
            }
        }

        throw new RuntimeException("Токен обновления не смог пройти валидацию, либо он уже является не актуальным");
    }

    @Operation(summary = "Обновление токена доступа")
    @PreAuthorize("isAnonymous()")
    @GetMapping("/getnewaccesstoken")
    public ResponseEntity<Map<String, String>> getNewRefreshToken(@CookieValue("refreshToken") Optional<String> refreshTokenFromCookie) {
        if (refreshTokenFromCookie.isPresent()) {
            String refreshToken = refreshTokenFromCookie.get();
            if (jwtUtil.verifyRefreshToken(refreshToken) && refreshTokenService.existsById(jwtUtil.getIdFromRefreshToken(refreshToken))) {
                RefreshToken refreshTokenFromDB = refreshTokenService.findById(jwtUtil.getIdFromRefreshToken(refreshToken));
                Person person = refreshTokenFromDB.getPerson();
                String accessToken = jwtUtil.generateAccessToken(person.getId(), person.getName(), person.getSurname(), person.getEmail(), person.getRole());
                return new ResponseEntity<>(Map.of("accessToken", accessToken), HttpStatus.OK);
            }
        }

        throw new RuntimeException("Токен обновления не смог пройти валидацию, либо он уже является не актуальным");
    }

    @GetMapping("/activateAccount")
    public ResponseEntity<?> activateAccount(@RequestParam("code") String activationCode) {
        personService.activate(activationCode);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/restorePassword")
    public ResponseEntity<?> restorePassword(@RequestBody String email) {
        personService.restorePassword(email);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam("token") String restoreToken, @RequestBody String password, @RequestBody String confirmingPassword) {
        personService.changePassword(restoreToken, password, confirmingPassword);

        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Map<String, String>> returnRefreshAndAccessTokens(HttpServletResponse response, RefreshToken refreshToken, Person person) {
        String refreshTokenGenerated = jwtUtil.generateRefreshToken(refreshToken.getId());

        Cookie cookieWithRefreshToken = new Cookie("refreshToken", refreshTokenGenerated);

        cookieWithRefreshToken.setMaxAge(cookie_max_age);

        response.addCookie(cookieWithRefreshToken);

        String accessTokenGenerated = jwtUtil.generateAccessToken(person.getId(), person.getName(), person.getSurname(), person.getEmail(), person.getRole());

        return new ResponseEntity<>(Map.of("accessToken", accessTokenGenerated), HttpStatus.OK);
    }
}
