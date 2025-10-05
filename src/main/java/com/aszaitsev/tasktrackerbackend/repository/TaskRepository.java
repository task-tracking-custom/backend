package com.aszaitsev.tasktrackerbackend.repository;

import com.aszaitsev.tasktrackerbackend.model.Task;
import com.aszaitsev.tasktrackerbackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findAllByDeadlineBefore(LocalDateTime deadline);
    Page<Task> findAllByDeadlineBefore(LocalDateTime deadline, Pageable pageable);

    List<Task> findAllByAssignee(User user);
    Page<Task> findAllByAssignee(User user, Pageable pageable);
}
