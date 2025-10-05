package com.aszaitsev.tasktrackerbackend.service.impl;

import com.aszaitsev.tasktrackerbackend.model.Project;
import com.aszaitsev.tasktrackerbackend.model.Task;
import com.aszaitsev.tasktrackerbackend.model.User;
import com.aszaitsev.tasktrackerbackend.repository.ProjectRepository;
import com.aszaitsev.tasktrackerbackend.service.ProjectService;
import com.aszaitsev.tasktrackerbackend.service.dto.request.CreateProjectRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repository;

    public ProjectServiceImpl(ProjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Task> tasksListByProjectId(UUID id) {
        return List.of(); // TODO
    }

    @Override
    public Page<Task> tasksListByProjectId(UUID id, Pageable pageable) {
        return null; // TODO
    }

    @Override
    public Optional<Project> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Project createProject(CreateProjectRequest dto, User owner) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setOwner(owner);
        
        return repository.save(project);
    }
}
