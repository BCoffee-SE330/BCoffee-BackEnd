package com.se330.coffee_shop_management_backend.service.notificationservices;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationForManyCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Notification;
import com.se330.coffee_shop_management_backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface INotificationService {
    Notification findByIdNotification(UUID id);
    Page<Notification> findAllNotifications(Pageable pageable);
    Page<Notification> findAllNotificationsByUserId(Pageable pageable);
    Page<Notification> findAllSentNotificationsByUserId(Pageable pageable);
    Page<Notification> findAllReceivedNotificationsByUserId(Pageable pageable);
    Notification createNotification(NotificationCreateRequestDTO notificationCreateRequestDTO);
    Notification updateNotification(NotificationUpdateRequestDTO notificationUpdateRequestDTO);
    Notification readNotification(UUID id);
    Page<Notification> sendNotificationToMany(NotificationForManyCreateRequestDTO notificationForManyCreateRequestDTO);
    Page<Notification> sendNotificationToAllUsers(NotificationCreateRequestDTO notificationCreateRequestDTO);
    void deleteNotification(UUID id);
    void addTokenToUser(String token);
    void removeTokenFromUser(String token);
    void sendLoginPushNotification(User user);
}
