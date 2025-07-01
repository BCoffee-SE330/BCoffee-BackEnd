package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationForManyCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.dto.response.notification.NotificationResponseDTO;
import com.se330.coffee_shop_management_backend.entity.Notification;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.SECURITY_SCHEME_NAME;
import static com.se330.coffee_shop_management_backend.util.CreatePageHelper.createPageable;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final INotificationService notificationService;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get notification detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved notification",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID format",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Notification not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<NotificationResponseDTO>> findByIdNotification(@PathVariable String id) {
        NotificationResponseDTO notification = NotificationResponseDTO.convert(notificationService.findByIdNotification(UUID.fromString(id)));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Notification retrieved successfully",
                        notification
                )
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Get all notifications with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved notification list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<NotificationResponseDTO>> findAllNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Notification> notificationPage = notificationService.findAllNotifications(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Notifications retrieved successfully",
                        NotificationResponseDTO.convert(notificationPage.getContent()),
                        new PageResponse.PagingResponse(
                                notificationPage.getNumber(),
                                notificationPage.getSize(),
                                notificationPage.getTotalElements(),
                                notificationPage.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(
            summary = "Create new notification",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Notification created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<NotificationResponseDTO>> createNotification(@RequestBody NotificationCreateRequestDTO notificationCreateRequestDTO) {
        NotificationResponseDTO notification = NotificationResponseDTO.convert(notificationService.createNotification(notificationCreateRequestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Notification created successfully",
                        notification
                )
        );
    }

    @PatchMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(
            summary = "Update notification",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Notification updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Notification not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<NotificationResponseDTO>> updateNotification(@RequestBody NotificationUpdateRequestDTO notificationUpdateRequestDTO) {
        NotificationResponseDTO notification = NotificationResponseDTO.convert(notificationService.updateNotification(notificationUpdateRequestDTO));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Notification updated successfully",
                        notification
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(
            summary = "Delete notification",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Notification deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Notification not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> deleteNotification(@PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    @Operation(
            summary = "Get all notifications for a user with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved notification list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<NotificationResponseDTO>> findAllNotificationsByUserId(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Notification> notificationPage = notificationService.findAllNotificationsByUserId(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "User notifications retrieved successfully",
                        NotificationResponseDTO.convert(notificationPage.getContent()),
                        new PageResponse.PagingResponse(
                                notificationPage.getNumber(),
                                notificationPage.getSize(),
                                notificationPage.getTotalElements(),
                                notificationPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/sent/user")
    @Operation(
            summary = "Get all sent notifications by a user with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved notification list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<NotificationResponseDTO>> findAllSentNotificationsByUserId(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Notification> notificationPage = notificationService.findAllSentNotificationsByUserId(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Sent notifications retrieved successfully",
                        NotificationResponseDTO.convert(notificationPage.getContent()),
                        new PageResponse.PagingResponse(
                                notificationPage.getNumber(),
                                notificationPage.getSize(),
                                notificationPage.getTotalElements(),
                                notificationPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/received/user")
    @Operation(
            summary = "Get all received notifications by a user with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved notification list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<NotificationResponseDTO>> findAllReceivedNotificationsByUserId(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Notification> notificationPage = notificationService.findAllReceivedNotificationsByUserId(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Received notifications retrieved successfully",
                        NotificationResponseDTO.convert(notificationPage.getContent()),
                        new PageResponse.PagingResponse(
                                notificationPage.getNumber(),
                                notificationPage.getSize(),
                                notificationPage.getTotalElements(),
                                notificationPage.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/send-to-many")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(
            summary = "Send notification to multiple users",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Notifications sent successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<NotificationResponseDTO>> sendNotificationToMany(
            @RequestBody NotificationForManyCreateRequestDTO notificationForManyCreateRequestDTO) {
        Page<Notification> notificationPage = notificationService.sendNotificationToMany(notificationForManyCreateRequestDTO);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Notifications sent to multiple users successfully",
                        NotificationResponseDTO.convert(notificationPage.getContent()),
                        new PageResponse.PagingResponse(
                                notificationPage.getNumber(),
                                notificationPage.getSize(),
                                notificationPage.getTotalElements(),
                                notificationPage.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/send-to-all")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(
            summary = "Send notification to all users",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Notifications sent successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<NotificationResponseDTO>> sendNotificationToAllUsers(
            @RequestBody NotificationCreateRequestDTO notificationCreateRequestDTO) {
        Page<Notification> notificationPage = notificationService.sendNotificationToAllUsers(notificationCreateRequestDTO);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Notifications sent to all users successfully",
                        NotificationResponseDTO.convert(notificationPage.getContent()),
                        new PageResponse.PagingResponse(
                                notificationPage.getNumber(),
                                notificationPage.getSize(),
                                notificationPage.getTotalElements(),
                                notificationPage.getTotalPages()
                        )
                )
        );
    }

    @PatchMapping("/read/{id}")
    @Operation(
            summary = "Mark notification as read",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Notification marked as read successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Notification not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<NotificationResponseDTO>> readNotification(@PathVariable String id) {
        UUID testUUID = UUID.fromString(id);
        NotificationResponseDTO notification = NotificationResponseDTO.convert(notificationService.readNotification(testUUID));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Notification marked as read successfully",
                        notification
                )
        );
    }
}