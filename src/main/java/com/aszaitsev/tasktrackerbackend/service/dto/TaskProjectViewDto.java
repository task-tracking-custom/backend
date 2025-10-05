package com.aszaitsev.tasktrackerbackend.service.dto;

import com.aszaitsev.tasktrackerbackend.model.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class TaskProjectViewDto {
    private UUID id;
    private String name;

    public UUID id() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskProjectViewDto(Project project) {
        this.id = project.getId();
        this.name = project.getName();
    }
}
