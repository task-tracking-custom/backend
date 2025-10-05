package com.aszaitsev.tasktrackerbackend.service.dto.request;

import com.aszaitsev.tasktrackerbackend.helper.validation.valid_status.ValidStatus;
import com.aszaitsev.tasktrackerbackend.model.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateTaskRequest {
    @NotBlank
    @Schema(example = "Task 1")
    private String title;
    @Schema(example = "Description")
    private String description;
    @ValidStatus
    @Schema(example = "NEW")
    private TaskStatus status;
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(example = "2030-01-01")
    private LocalDate deadline;
    @NotNull
    private UUID projectId;
}