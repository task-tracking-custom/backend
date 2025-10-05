package com.aszaitsev.tasktrackerbackend.helper.validation.valid_status;

import com.aszaitsev.tasktrackerbackend.model.TaskStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class ValidStatusValidator implements ConstraintValidator<ValidStatus, TaskStatus> {
    @Override
    public void initialize(ValidStatus constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TaskStatus s, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.asList(TaskStatus.values()).contains(s);
    }
}
