package com.example.ToDoApp.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {

    @Schema(description = "Имя пользователя", example = "Piter")
    @Size(min=5, max=20)
    @NotBlank(message = "Имя не может быть пустым")
    private String username;

    @Schema(description = "Адрес эл.почты")
    @Size(min = 5, max = 255)
    @NotBlank(message = "Адрес эл.почты не может быть пустым")
    @Email(message = "Email формата yourname@example.com")
    private String email;

    @Schema(description = "Пароль")
    @Size(max = 255)
    private String password;
}
