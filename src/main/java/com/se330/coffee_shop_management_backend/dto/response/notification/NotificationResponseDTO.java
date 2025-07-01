package com.se330.coffee_shop_management_backend.dto.response.notification;

import com.se330.coffee_shop_management_backend.entity.Notification;
import com.se330.coffee_shop_management_backend.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@SuperBuilder
public class NotificationResponseDTO {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    private String notificationType;
    private String notificationContent;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private boolean isRead;

    @Schema(
            name = "createdAt",
            description = "Date time field of employee creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of employee update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    public static NotificationResponseDTO convert(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId().toString())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .notificationType(notification.getNotificationType().getValue())
                .notificationContent(notification.getNotificationContent())
                .senderId(notification.getSender() != null ? notification.getSender().getId().toString() : null)
                .senderName(notification.getSender() != null ? notification.getSender().getFullName() : null)
                .receiverId(notification.getReceiver() != null ? notification.getReceiver().getId().toString() : null)
                .receiverName(notification.getReceiver() != null ? notification.getReceiver().getFullName() : null)
                .isRead(notification.isRead())
                .build();
    }

    public static List<NotificationResponseDTO> convert(List<Notification> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            return Collections.emptyList();
        }

        return notifications.stream()
                .map(NotificationResponseDTO::convert)
                .collect(Collectors.toList());
    }
}
