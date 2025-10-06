package com.aszaitsev.tasktrackerbackend.service.dto;

import com.aszaitsev.tasktrackerbackend.model.Task;
import com.aszaitsev.tasktrackerbackend.model.TaskPriority;
import com.aszaitsev.tasktrackerbackend.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "Отображение задачи в списке")
public class TaskViewDto {
    private String title;
    private TaskStatus status;
    private TaskPriority priority;
    private String projectName;
    private String assignee;
    private LocalDateTime deadline;

    public TaskViewDto(Task task) {
        this.title = task.getTitle();
        this.status = task.getStatus();
        this.priority = task.getPriority();
        this.projectName = task.getProject().getName();
        this.assignee = task.getAssignee().getUsername();
        this.deadline = task.getDeadline();
    }

    public String title() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String projectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String assignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
}
