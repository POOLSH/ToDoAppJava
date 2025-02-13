package com.example.ToDoApp.services;

import com.example.ToDoApp.DTO.JwtAuthenticationResponse;
import com.example.ToDoApp.DTO.SignInRequest;
import com.example.ToDoApp.DTO.SignUpRequest;
import com.example.ToDoApp.models.Role;
import com.example.ToDoApp.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(SignUpRequest signUpRequest) {

        var user= User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.createUser(user);

        var jwt=jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getUsername(),
                signInRequest.getPassword()
        ));

        var user=userService.userDetailsService().loadUserByUsername(signInRequest.getUsername());

        SecurityContextHolder.clearContext();

        var jwt=jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
