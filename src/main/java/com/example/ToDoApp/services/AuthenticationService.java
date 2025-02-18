package com.example.ToDoApp.services;

import com.example.ToDoApp.DTO.JwtAuthenticationResponse;
import com.example.ToDoApp.DTO.RefreshTokenRequest;
import com.example.ToDoApp.DTO.SignInRequest;
import com.example.ToDoApp.DTO.SignUpRequest;
import com.example.ToDoApp.models.Role;
import com.example.ToDoApp.models.User;
import com.example.ToDoApp.models.RefreshToken;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    /**
     * Регистрация пользователя
     */
    public JwtAuthenticationResponse signUp(SignUpRequest signUpRequest) {
        var user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.createUser(user);

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user);

        return new JwtAuthenticationResponse(accessToken, refreshToken.getToken());
    }

    /**
     * Аутентификация пользователя (вход в систему)
     */
    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsername(),
                        signInRequest.getPassword()
                )
        );

        var user = userService.getByUsername(signInRequest.getUsername());

        SecurityContextHolder.clearContext();

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user);

        return new JwtAuthenticationResponse(accessToken, refreshToken.getToken());
    }

    /**
     * Обновление access-токена по refresh-токену
     */
    @Transactional
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest request) {
        var refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        // Проверяем, не истек ли refresh-токен
        if (refreshTokenService.isRefreshTokenExpired(refreshToken)) {
            refreshTokenService.deleteByUser(refreshToken.getUser());
            throw new RuntimeException("Refresh token expired, please login again");
        }

        var user = refreshToken.getUser();

        // Удаляем старый refresh-токен и создаём новый
        refreshTokenService.deleteByUser(user);
        var newRefreshToken = refreshTokenService.createRefreshToken(user);

        var newAccessToken = jwtService.generateAccessToken(user);

        return new JwtAuthenticationResponse(newAccessToken, newRefreshToken.getToken());
    }

    /**
     * Выход из системы (удаление refresh-токена)
     */
    @Transactional
    public void logout(String refreshToken) {
        var token = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        refreshTokenService.deleteByUser(token.getUser());
    }
}
