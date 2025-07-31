package com.aszaitsev.tasktrackerbackend.repository;

import com.aszaitsev.tasktrackerbackend.model.GithubUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubUserRepository extends JpaRepository<GithubUser, Integer> {
}
