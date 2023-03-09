package ru.Art3m1y.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.RefreshToken;
import ru.Art3m1y.shop.repositories.RefreshTokenRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public boolean existsById(long id) {
       return refreshTokenRepository.existsById(id);
    }

    @Transactional
    public void deleteById(long id) {
        refreshTokenRepository.deleteById(id);
    }

    @Transactional
    public void updateRefreshToken(Person person, RefreshToken refreshTokenUpdated) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByPerson(person);

        if (refreshToken.isPresent()) {
            deleteById(refreshToken.get().getId());
        }

        save(refreshTokenUpdated);
    }

    @Transactional(readOnly = true)
    public RefreshToken findById(long id) {
        return refreshTokenRepository.findById(id).orElseThrow(() -> new RuntimeException("Токен обновления не найден в базе данных"));
    }
}
