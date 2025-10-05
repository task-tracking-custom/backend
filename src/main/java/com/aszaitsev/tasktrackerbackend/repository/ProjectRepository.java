package com.aszaitsev.tasktrackerbackend.repository;

import com.aszaitsev.tasktrackerbackend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
}
