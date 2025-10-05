package com.aszaitsev.tasktrackerbackend.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String id) {
        super(String.format("Не найдена задача %s", id));
    }
}
