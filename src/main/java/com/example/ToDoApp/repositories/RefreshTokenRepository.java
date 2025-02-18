package com.example.ToDoApp.repositories;

import com.example.ToDoApp.models.RefreshToken;
import com.example.ToDoApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);


    void deleteByUser(User user);
}
