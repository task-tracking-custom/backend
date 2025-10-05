package com.aszaitsev.tasktrackerbackend.service.dto;

import com.aszaitsev.tasktrackerbackend.model.Task;
import com.aszaitsev.tasktrackerbackend.model.TaskPriority;
import com.aszaitsev.tasktrackerbackend.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Schema(description = "Подробная информация на странице задачи")
public class TaskDetailsDto {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private TaskProjectViewDto project;
    private TaskUserViewDto assignee;
    private TaskUserViewDto creator;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TaskDetailsDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.priority = task.getPriority();
        this.project = new TaskProjectViewDto(task.getProject());
        this.assignee = new TaskUserViewDto(task.getAssignee());
        this.creator = new TaskUserViewDto(task.getCreator());
        this.deadline = task.getDeadline();
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
    }

    public UUID id() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String title() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String description() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus status() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority priority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskProjectViewDto project() {
        return project;
    }

    public void setProject(TaskProjectViewDto project) {
        this.project = project;
    }

    public TaskUserViewDto assignee() {
        return assignee;
    }

    public void setAssignee(TaskUserViewDto assignee) {
        this.assignee = assignee;
    }

    public TaskUserViewDto creator() {
        return creator;
    }

    public void setCreator(TaskUserViewDto creator) {
        this.creator = creator;
    }

    public LocalDateTime deadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
