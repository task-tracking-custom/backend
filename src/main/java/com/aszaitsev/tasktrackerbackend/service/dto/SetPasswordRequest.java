package com.aszaitsev.tasktrackerbackend.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на установку пароля для OAuth пользователя")
public class SetPasswordRequest {
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Новое имя пользователя", example = "john_doe", required = true, minLength = 3, maxLength = 50)
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Schema(description = "Пароль", example = "password123", required = true, minLength = 6, maxLength = 100)
    private String password;
    
    // Конструкторы
    public SetPasswordRequest() {}
    
    public SetPasswordRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Геттеры и сеттеры
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