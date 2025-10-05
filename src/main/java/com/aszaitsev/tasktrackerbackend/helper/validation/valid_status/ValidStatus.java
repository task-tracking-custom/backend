package com.aszaitsev.tasktrackerbackend.helper.validation.valid_status;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidStatusValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStatus {
    String message() default "Некорректный статус задачи!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
