package com.se330.coffee_shop_management_backend.dto.request.notification;

import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NotificationUpdateRequestDTO {
    private UUID notificationId;
    private Constants.NotificationTypeEnum notificationType;
    private String notificationContent;
    private UUID senderId;
    private UUID receiverId;
    private boolean isRead;
}
