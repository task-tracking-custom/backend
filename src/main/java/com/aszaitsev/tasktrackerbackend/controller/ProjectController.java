package com.aszaitsev.tasktrackerbackend.controller;

import com.aszaitsev.tasktrackerbackend.model.Project;
import com.aszaitsev.tasktrackerbackend.security.CustomUserDetails;
import com.aszaitsev.tasktrackerbackend.service.ProjectService;
import com.aszaitsev.tasktrackerbackend.service.UserService;
import com.aszaitsev.tasktrackerbackend.service.dto.request.CreateProjectRequest;
import com.aszaitsev.tasktrackerbackend.service.dto.ProjectViewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aszaitsev.tasktrackerbackend.model.User;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
@Tag(name = "Project Management", description = "API для управления проектами")
@SecurityRequirement(name = "Bearer Authentication")
public class ProjectController {

    private final ProjectService service;
    private final UserService userService;

    public ProjectController(ProjectService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/projects")
    @Operation(
            summary = "Создать новый проект",
            description = "Создает новый проект с указанным именем и описанием. Текущий пользователь становится владельцем проекта."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Проект успешно создан",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectViewDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные данные запроса",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content
            )
    })
    public ResponseEntity<?> createProject(@Valid @RequestBody CreateProjectRequest dto, @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
        Project project = service.createProject(
                dto,
                userService.getUserByUsername(userDetails.getUsername())

        );
        return ResponseEntity.created(
                UriComponentsBuilder
                        .fromPath("/api/projects/{id}")
                        .buildAndExpand(project.getId()).toUri()
        ).body(new ProjectViewDto(project));
    }
}
