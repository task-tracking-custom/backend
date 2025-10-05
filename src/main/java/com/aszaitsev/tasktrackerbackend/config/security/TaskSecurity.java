package com.aszaitsev.tasktrackerbackend.config.security;

import com.aszaitsev.tasktrackerbackend.model.Project;
import com.aszaitsev.tasktrackerbackend.model.Task;
import com.aszaitsev.tasktrackerbackend.service.ProjectService;
import com.aszaitsev.tasktrackerbackend.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("taskSecurity")
public class TaskSecurity {
    private final ProjectService projectService;
    private final TaskService taskService;

    public TaskSecurity(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    public boolean isProjectOwner(UUID projectId, Authentication authentication) {
        String username = authentication.getName();
        Optional<Project> projectOpt = projectService.findById(projectId);
        return projectOpt.isPresent() && projectOpt.get().getOwner().getUsername().equals(username);
    }

    public boolean isTaskOwner(UUID taskId, Authentication authentication) {
        String username = authentication.getName();
        Optional<Task> taskOpt = taskService.findById(taskId);
        return taskOpt.isPresent() &&
                (taskOpt.get().getCreator().getUsername().equals(username) ||
                taskOpt.get().getAssignee().getUsername().equals(username));
    }
}
