package com.aszaitsev.tasktrackerbackend.service.dto.response;

import com.aszaitsev.tasktrackerbackend.service.dto.FieldError;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Schema(description = "Ответ с ошибкой")
public class ErrorResponse {
    @Schema(example = "400")
    private Integer status;

    @Schema(example = "Errormessage")
    private String message;

    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    @Schema(example = "2030-01-01T00:00:00")
    private String timestamp;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FieldError> errors = null;

    public void addError(FieldError fieldError) {
        this.errors.add(fieldError);
    }
}
