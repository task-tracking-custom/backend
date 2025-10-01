package com.aszaitsev.tasktrackerbackend.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с сообщением")
public class MessageResponse {
    
    @Schema(description = "Текст сообщения", example = "Operation completed successfully")
    private String message;
    
    // Конструкторы
    public MessageResponse() {}
    
    public MessageResponse(String message) {
        this.message = message;
    }
    
    // Геттеры и сеттеры
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}