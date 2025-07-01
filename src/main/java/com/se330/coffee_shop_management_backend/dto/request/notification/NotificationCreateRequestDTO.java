package com.se330.coffee_shop_management_backend.dto.request.notification;

import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationCreateRequestDTO {
    private Constants.NotificationTypeEnum notificationType;
    private String notificationContent;
    private UUID senderId;
    private UUID receiverId;
    private boolean isRead;
}