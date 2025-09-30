package com.aszaitsev.tasktrackerbackend.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с JWT токеном")
public class AuthResponse {
    
    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Тип токена", example = "Bearer")
    private String type = "Bearer";
    
    @Schema(description = "Имя пользователя", example = "john_doe")
    private String username;
    
    @Schema(description = "Email пользователя", example = "john@example.com")
    private String email;
    
    // Конструкторы
    public AuthResponse() {}
    
    public AuthResponse(String token, String type, String username, String email) {
        this.token = token;
        this.type = type != null ? type : "Bearer";
        this.username = username;
        this.email = email;
    }
    
    // Геттеры и сеттеры
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
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
}