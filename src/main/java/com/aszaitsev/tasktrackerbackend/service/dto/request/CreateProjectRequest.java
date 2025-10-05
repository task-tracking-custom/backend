package com.aszaitsev.tasktrackerbackend.service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на создание нового проекта")
public class CreateProjectRequest {
    
    @Schema(
            description = "Название проекта",
            example = "Task Tracker Backend",
            required = true
    )
    @NotBlank(message = "Название проекта не может быть пустым")
    @Size(min = 3, max = 255, message = "Название проекта должно быть от 3 до 255 символов")
    private String name;
    
    @Schema(
            description = "Описание проекта",
            example = "Backend приложение для управления задачами и проектами с поддержкой REST API"
    )
    @Size(max = 2000, message = "Описание не может быть длиннее 2000 символов")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
