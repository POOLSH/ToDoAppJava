package com.example.ToDoApp.services;

import com.example.ToDoApp.models.RefreshToken;
import com.example.ToDoApp.models.User;
import com.example.ToDoApp.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Создание refresh-токена
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public RefreshToken createRefreshToken(User user) {
        // Удаляем старый refresh-токен перед созданием нового
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(60 * 60 * 24 * 7)) // 7 дней
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Поиск refresh-токена по строке
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Проверка на истечение refresh-токена
     */
    public boolean isRefreshTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    /**
     * Удаление refresh-токена по пользователю
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
