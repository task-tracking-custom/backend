package com.aszaitsev.tasktrackerbackend.service.impl;

import com.aszaitsev.tasktrackerbackend.exception.TaskNotFoundException;
import com.aszaitsev.tasktrackerbackend.model.Task;
import com.aszaitsev.tasktrackerbackend.model.TaskStatus;
import com.aszaitsev.tasktrackerbackend.model.User;
import com.aszaitsev.tasktrackerbackend.repository.TaskRepository;
import com.aszaitsev.tasktrackerbackend.service.TaskService;
import com.aszaitsev.tasktrackerbackend.service.dto.TaskDetailsDto;
import com.aszaitsev.tasktrackerbackend.service.dto.TaskViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Task createTask(Task task) {
        return repository.save(task);
    }

    @Override
    public List<TaskViewDto> listByDeadline(LocalDateTime deadline) {
        return repository.findAllByDeadlineBefore(deadline).stream().map(TaskViewDto::new).collect(Collectors.toList());
    }

    @Override
    public Page<TaskViewDto> listByDeadline(LocalDateTime deadline, Pageable pageable) {
        return repository.findAllByDeadlineBefore(deadline, pageable).map(TaskViewDto::new);
    }

    @Override
    public List<TaskViewDto> listByUser(User user) {
        return repository.findAllByAssignee(user).stream().map(TaskViewDto::new).collect(Collectors.toList());
    }

    @Override
    public Page<TaskViewDto> listByUser(User user, Pageable pageable) {
        return repository.findAllByAssignee(user, pageable).map(TaskViewDto::new);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public TaskDetailsDto findViewById(UUID id) {
        return repository.findById(id)
                .map(TaskDetailsDto::new)
                .orElseThrow(
                        () -> new TaskNotFoundException(id.toString())
                );
    }

    @Override
    public void changeStatus(UUID id, TaskStatus status) {
        repository.findById(id).map(task -> {
            task.setStatus(status);
            repository.save(task);
            return task;
        }).orElseThrow(() -> new TaskNotFoundException(id.toString()));
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
