package ru.Art3m1y.shop.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.Art3m1y.shop.models.RefreshToken;
import ru.Art3m1y.shop.repositories.RefreshTokenRepository;
import ru.Art3m1y.shop.services.RefreshTokenService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void save_shouldSaveRefreshToken_whenUserLogins() {
        RefreshToken refreshToken = new RefreshToken(1);

        refreshTokenService.save(refreshToken);

        verify(refreshTokenRepository, times(1)).save(refreshToken);
    }

    @Test
    void existsById_shouldReturnBooleanValueOfExistsRefreshTokenById_whenUserUploadsAccessToken() {
        long id = 1;

        when(refreshTokenRepository.existsById(id)).thenReturn(true);

        boolean isExist = refreshTokenService.existsById(id);

        verify(refreshTokenRepository, times(1)).existsById(id);
        assertTrue(isExist);
    }

    @Test
    void deleteById_shouldDeleteRefreshTokenById_whenUserLogouts() {
        long id = 1;

        refreshTokenService.deleteById(id);

        verify(refreshTokenRepository).deleteById(id);
    }

    @Test
    void updateRefreshToken() {
        long id = 1;




    }

    @Test
    void findById_shouldFindRefreshTokenById_whenUserUpdatesAccessToken() {
        long id = 1;
        RefreshToken refreshToken = mock(RefreshToken.class);
        when(refreshTokenRepository.findById(id)).thenReturn(Optional.of(refreshToken));

        RefreshToken refreshTokenGot = refreshTokenService.findById(id);

        verify(refreshTokenRepository, times(1)).findById(id);
        assertNotNull(refreshToken);
        assertEquals(refreshToken, refreshTokenGot);
    }
}