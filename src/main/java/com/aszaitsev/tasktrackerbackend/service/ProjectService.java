package com.aszaitsev.tasktrackerbackend.service;

import com.aszaitsev.tasktrackerbackend.model.Project;
import com.aszaitsev.tasktrackerbackend.model.Task;
import com.aszaitsev.tasktrackerbackend.model.User;
import com.aszaitsev.tasktrackerbackend.service.dto.request.CreateProjectRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectService {
    List<Task> tasksListByProjectId(UUID id);
    Page<Task> tasksListByProjectId(UUID id, Pageable pageable);
    Optional<Project> findById(UUID id);
    Project createProject(CreateProjectRequest dto, User owner);
}
