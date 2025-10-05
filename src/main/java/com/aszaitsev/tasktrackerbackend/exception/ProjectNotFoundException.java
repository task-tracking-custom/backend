package com.aszaitsev.tasktrackerbackend.exception;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(@NotBlank UUID projectId) {
        super(String.format("Не найден проект с id %s", projectId.toString()));
    }
}
