package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Notification;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class NotificationAdminResponse {
    // Mã của thông báo
    private String notificationId;
    // Loại thông báo (Chỉ có ORDER("ĐƠN HÀNG"),PAYMENT("THANH TOÁN"),DISCOUNT("KHUYẾN MÃI"),SYSTEM("HỆ THỐNG"),INVENTORY("KHO HÀNG"),EMPLOYEE("NHÂN SỰ"),MANAGER("QUẢN LÝ"),INVOICE("KHO HÀNG"),TRANSFER("CHUYỂN KHO"),BRANCH("CHI NHÁNH"),SUPPLIER("NHÀ CUNG CẤP"),PRODUCT("SẢN PHẨM"),WAREHOUSE("NHÀ KHO"))
    private Constants.NotificationTypeEnum notificationType;
    // Nội dung thông báo
    private String notificationContent;
    // Mã người gửi, khóa ngoại tham chiếu đến bảng User
    // Chỉ có manager được gửi thông báo, còn lại thì sender sẽ là null
    private String senderId;
    // Mã người nhận, khóa ngoại tham chiếu đến bảng User
    private String receiverId;
    // Thông báo đã được đọc hay chưa
    private boolean isRead;

    public static NotificationAdminResponse convert(Notification notification) {
        return NotificationAdminResponse.builder()
                .notificationId(notification.getId().toString())
                .notificationType(notification.getNotificationType())
                .notificationContent(notification.getNotificationContent())
                .senderId(notification.getSender() != null ? notification.getSender().getId().toString() : null)
                .receiverId(notification.getReceiver().getId().toString())
                .isRead(notification.isRead())
                .build();
    }

    public static List<NotificationAdminResponse> convert(List<Notification> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            return List.of();
        }

        return notifications.stream()
                .map(NotificationAdminResponse::convert)
                .toList();
    }
}

