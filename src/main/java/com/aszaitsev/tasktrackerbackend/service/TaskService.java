package com.aszaitsev.tasktrackerbackend.service;

import com.aszaitsev.tasktrackerbackend.model.Task;
import com.aszaitsev.tasktrackerbackend.model.TaskStatus;
import com.aszaitsev.tasktrackerbackend.model.User;
import com.aszaitsev.tasktrackerbackend.service.dto.TaskDetailsDto;
import com.aszaitsev.tasktrackerbackend.service.dto.TaskViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    Task createTask(Task task);

    List<TaskViewDto> listByDeadline(LocalDateTime deadline);
    Page<TaskViewDto> listByDeadline(LocalDateTime deadline, Pageable pageable);

    List<TaskViewDto> listByUser(User user);
    Page<TaskViewDto> listByUser(User user, Pageable pageable);

    Optional<Task> findById(UUID id);
    TaskDetailsDto findViewById(UUID id);

    void changeStatus(UUID id, TaskStatus status);

    void delete(UUID id);
}
