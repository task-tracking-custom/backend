package com.aszaitsev.tasktrackerbackend.controller;

import com.aszaitsev.tasktrackerbackend.model.OAuthProvider;
import com.aszaitsev.tasktrackerbackend.model.User;
import com.aszaitsev.tasktrackerbackend.repository.UserOAuthLinkRepository;
import com.aszaitsev.tasktrackerbackend.repository.UserRepository;
import com.aszaitsev.tasktrackerbackend.security.CustomUserDetails;
import com.aszaitsev.tasktrackerbackend.service.UserService;
import com.aszaitsev.tasktrackerbackend.service.dto.TaskViewDto;
import com.aszaitsev.tasktrackerbackend.service.dto.response.ErrorResponse;
import com.aszaitsev.tasktrackerbackend.service.dto.response.MessageResponse;
import com.aszaitsev.tasktrackerbackend.service.dto.request.SetPasswordRequest;
import com.aszaitsev.tasktrackerbackend.service.dto.response.UserInfoResponse;
import com.aszaitsev.tasktrackerbackend.service.impl.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "User Management", description = "API для управления профилем пользователя и OAuth привязками")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserOAuthLinkRepository userOAuthLinkRepository;
    @Autowired
    private TaskServiceImpl taskService;

    @GetMapping("/me")
    @Operation(
            summary = "Получить информацию о текущем пользователе",
            description = "Возвращает подробную информацию о текущем авторизованном пользователе, включая привязанные OAuth аккаунты"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный запрос",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content
            )
    })
    public ResponseEntity<UserInfoResponse> getCurrentUser(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserInfoResponse userInfo = userService.getUserInfo(userDetails.getUsername());
        return ResponseEntity.ok(userInfo);
    }
    
    @PostMapping("/set-password")
    @Operation(
            summary = "Установить пароль для OAuth пользователя",
            description = "Позволяет пользователям, зарегистрированным через OAuth, установить пароль для входа без OAuth"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пароль успешно установлен",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные данные или username уже занят",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content
            )
    })
    public ResponseEntity<MessageResponse> setPassword(
            @Valid @RequestBody SetPasswordRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        userService.setPasswordAndUsername(user, request.getUsername(), request.getPassword());
        
        return ResponseEntity.ok(new MessageResponse(
                "Password and username set successfully"
        ));
    }
    
    @PostMapping("/link-oauth/{provider}")
    @Operation(
            summary = "Привязать OAuth аккаунт",
            description = "Инициирует процесс привязки OAuth аккаунта (GitHub или Google) к текущему пользователю"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Редирект на OAuth провайдер",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content
            )
    })
    public ResponseEntity<MessageResponse> initiateLinkOAuth(
            @Parameter(description = "OAuth провайдер (github или google)", example = "github")
            @PathVariable String provider,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpSession session) {
        
        // Сохраняем в сессию информацию о режиме привязки
        session.setAttribute("OAUTH_LINK_MODE", true);
        session.setAttribute("OAUTH_LINK_USER", userDetails.getUsername());
        
        String redirectUrl = "/oauth2/authorization/" + provider.toLowerCase();
        return ResponseEntity.ok(new MessageResponse("Redirect to: " + redirectUrl));
    }
    
    @DeleteMapping("/unlink-oauth/{provider}")
    @Transactional
    @Operation(
            summary = "Отвязать OAuth аккаунт",
            description = "Удаляет привязку OAuth аккаунта. Нельзя отвязать последний способ входа."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OAuth аккаунт успешно отвязан",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Нельзя отвязать последний способ входа",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не авторизован",
                    content = @Content
            )
    })
    public ResponseEntity<MessageResponse> unlinkOAuth(
            @Parameter(description = "OAuth провайдер (github или google)", example = "github")
            @PathVariable String provider,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        OAuthProvider oauthProvider = OAuthProvider.valueOf(provider.toUpperCase());
        
        // Проверка: не последний ли это способ авторизации
        boolean hasPassword = user.getPassword() != null && !user.getPassword().isEmpty();
        long oauthLinksCount = userOAuthLinkRepository.findByUser(user).size();
        
        if (!hasPassword && oauthLinksCount <= 1) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Cannot unlink the last authentication method"));
        }
        
        userOAuthLinkRepository.deleteByUserAndProvider(user, oauthProvider);
        
        return ResponseEntity.ok(new MessageResponse(provider + " account unlinked successfully"));
    }

    @GetMapping("/me/tasks")
    @Operation(
            summary = "Получить список задач для авторизованного пользователя",
            description = "Возвращает список задач для авторизованного пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список задач по пользователю",
                    content = @Content(
                            schema = @Schema(implementation = TaskViewDto.class),
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Не найден пользователь",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<?> myTasks(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(taskService.listByUser(user));
    }
}