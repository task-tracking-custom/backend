package com.aszaitsev.tasktrackerbackend.repository;

import com.aszaitsev.tasktrackerbackend.model.GoogleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleUserRepository extends JpaRepository<GoogleUser, String> {
}
