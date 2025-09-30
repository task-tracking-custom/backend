package com.aszaitsev.tasktrackerbackend.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на регистрацию нового пользователя")
public class RegisterRequest {
    
    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "Имя пользователя", example = "Funfayk082", required = true, minLength = 3, maxLength = 50)
    private String username;

    @NotBlank
    @Email
    @Schema(description = "Email адрес", example = "Funfayk082@example.com", required = true)
    private String email;

    @NotBlank
    @Size(min = 6)
    @Schema(description = "Пароль", example = "password123", required = true, minLength = 6)
    private String password;

    @Schema(description = "Имя", example = "Andrey")
    private String firstName;
    
    @Schema(description = "Фамилия", example = "Zaitsev")
    private String lastName;

    public RegisterRequest() {}

    public RegisterRequest(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}