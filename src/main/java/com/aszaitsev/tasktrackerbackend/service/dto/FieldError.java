package com.aszaitsev.tasktrackerbackend.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FieldError {
    @Schema(example = "Field1")
    private String field;

    @Schema(example = "Errormessage1")
    private String message;
}

