package com.aszaitsev.tasktrackerbackend.service.dto;

import com.aszaitsev.tasktrackerbackend.model.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о проекте")
public class ProjectViewDto {
    
    @Schema(description = "ID проекта", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    
    @Schema(description = "Название проекта", example = "Task Tracker Backend")
    private String name;
    
    @Schema(description = "Описание проекта", example = "Backend приложение для управления задачами и проектами с поддержкой REST API")
    private String description;
    
    @Schema(description = "ID владельца проекта", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID ownerId;
    
    public ProjectViewDto(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.ownerId = project.getOwner() != null ? project.getOwner().getId() : null;
    }
}
