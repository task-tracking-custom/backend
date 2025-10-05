package com.aszaitsev.tasktrackerbackend.controller;

import com.aszaitsev.tasktrackerbackend.exception.ProjectNotFoundException;
import com.aszaitsev.tasktrackerbackend.model.Task;
import com.aszaitsev.tasktrackerbackend.model.TaskStatus;
import com.aszaitsev.tasktrackerbackend.model.User;
import com.aszaitsev.tasktrackerbackend.security.CustomUserDetails;
import com.aszaitsev.tasktrackerbackend.service.ProjectService;
import com.aszaitsev.tasktrackerbackend.service.TaskService;
import com.aszaitsev.tasktrackerbackend.service.UserService;
import com.aszaitsev.tasktrackerbackend.service.dto.TaskDetailsDto;
import com.aszaitsev.tasktrackerbackend.service.dto.TaskViewDto;
import com.aszaitsev.tasktrackerbackend.service.dto.request.CreateTaskRequest;
import com.aszaitsev.tasktrackerbackend.service.dto.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskService service;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;


    @PostMapping("/tasks")
    @Operation(
            summary = "Создать задачу",
            description = "Возвращает созданную задачу"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Задача успешно создана",
                    content = @Content(
                            schema = @Schema(implementation = Task.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Проект не найден",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные данные",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<?> createTask(@RequestBody @Valid CreateTaskRequest dto, @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
        return projectService.findById(dto.getProjectId()).map(project -> {
            User user = userService.getUserByUsername(userDetails.getUsername());
            return ResponseEntity.ok(new TaskDetailsDto(service.createTask(
                    new Task(
                            dto.getTitle(),
                            dto.getDescription(),
                            project,
                            user,
                            dto.getDeadline().atStartOfDay()
                    )
            )));
        }).orElseThrow(() -> new ProjectNotFoundException(dto.getProjectId()));
    }

    @PreAuthorize("hasRole('ADMIN') or @taskSecurity.isTaskOwner(#id, authentication)")
    @PatchMapping("/tasks/{id}/status")
    @Operation(
            summary = "Изменить статус задачи",
            description = "Возвращает сообщение об успешном изменении статуса"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Статус успешно изменён",
                    content = @Content(
                            mediaType = "application/text"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Задача не найдена",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные данные",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<?> changeStatus(@PathVariable UUID id, @RequestParam @Valid TaskStatus status) {
        service.changeStatus(id, status);
        return ResponseEntity.ok("Статус успешно изменён!");
    }


    @PreAuthorize("hasRole('ADMIN') or @taskSecurity.isTaskOwner(#id, authentication)")
    @DeleteMapping("/tasks/{id}")
    @Operation(
            summary = "Удалить задачу",
            description = "Возвращает сообщение об успешном удалении задачи"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Задача успешно удалена",
                    content = @Content(
                            mediaType = "application/text"
                    )
            )
    })
    public ResponseEntity<?> deleteTask(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Задача успешно удалена!");
    }



    @GetMapping("/tasks")
    @Operation(
            summary = "Получить список задач до дедлайна",
            description = "Возвращает полный список задач, у которых дедлайн до указанной даты (с кэшированием)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные успешно получены",
                    content = @Content(
                            schema = @Schema(implementation = TaskViewDto.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные данные",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    @Cacheable(value = "tasksByDeadline", key = "#deadline.toString()")
    public ResponseEntity<List<TaskViewDto>> listByDeadline(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate deadline
    ) {
        return ResponseEntity.ok(service.listByDeadline(deadline.atStartOfDay()));
    }

    @GetMapping("/tasks/paginated")
    @Operation(
            summary = "Получить список задач до дедлайна с пагинацией",
            description = "Возвращает пагинированный список задач, у которых дедлайн до указанной даты"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные успешно получены",
                    content = @Content(
                            schema = @Schema(implementation = Page.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные данные",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<Page<TaskViewDto>> listByDeadlinePaginated(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate deadline,
            @PageableDefault(size = 10, sort = "deadline") Pageable pageable
    ) {
        return ResponseEntity.ok(service.listByDeadline(deadline.atStartOfDay(), pageable));
    }
}
