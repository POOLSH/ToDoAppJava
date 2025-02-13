package com.example.ToDoApp.services;

import com.example.ToDoApp.models.Role;
import com.example.ToDoApp.models.User;
import com.example.ToDoApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User createUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return saveUser(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Auth details: " + auth);
        System.out.println("Authorities: " + auth.getAuthorities());

        var userName = auth.getName();
        return getByUsername(userName);
    }


    @Deprecated
    public void getAdmin(){
        var user=getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        saveUser(user);
    }
}
