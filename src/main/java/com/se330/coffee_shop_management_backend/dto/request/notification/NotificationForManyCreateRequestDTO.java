package com.se330.coffee_shop_management_backend.dto.request.notification;

import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationForManyCreateRequestDTO {
    private Constants.NotificationTypeEnum notificationType;
    private String notificationContent;
    private List<UUID> receiverId;
    private boolean isRead;
}
