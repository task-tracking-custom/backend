package com.aszaitsev.tasktrackerbackend.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на вход в систему")
public class LoginRequest {
    
    @NotBlank
    @Schema(description = "Имя пользователя", example = "john_doe", required = true)
    private String username;

    @NotBlank
    @Schema(description = "Пароль", example = "password123", required = true)
    private String password;

    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}