package com.se330.coffee_shop_management_backend.service.notificationservices.imp;

import com.google.firebase.messaging.*;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationForManyCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Notification;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.entity.UserRecipientToken;
import com.se330.coffee_shop_management_backend.repository.NotificationRepository;
import com.se330.coffee_shop_management_backend.repository.UserRecipientTokenRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.service.UserService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ImpNotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRecipientTokenRepository userRecipientTokenRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final UserService userService;
    private final UserRepository userRepository;

    public ImpNotificationService(
            NotificationRepository notificationRepository,
            UserRecipientTokenRepository userRecipientTokenRepository,
            UserService userService,
            FirebaseMessaging firebaseMessaging,
            UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.userRecipientTokenRepository = userRecipientTokenRepository;
        this.firebaseMessaging = firebaseMessaging;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Notification findByIdNotification(UUID id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> findAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> findAllNotificationsByUserId(Pageable pageable) {
        User user = userService.getUser();
        return notificationRepository.findAllByUser(user, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> findAllSentNotificationsByUserId(Pageable pageable) {
        User sender = userService.getUser();
        return notificationRepository.findAllBySender(sender, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> findAllReceivedNotificationsByUserId(Pageable pageable) {
        User recipient = userService.getUser();
        return notificationRepository.findAllByReceiver(recipient, pageable);
    }

    @Override
    @Transactional
    public Notification createNotification(NotificationCreateRequestDTO notificationCreateRequestDTO) {

        User sender = null;

        if (notificationCreateRequestDTO.getSenderId() != null) {
            sender = userRepository.findById(notificationCreateRequestDTO.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
        }

        User receiver = userRepository.findById(notificationCreateRequestDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Notification newNoti = notificationRepository.save(
                Notification.builder()
                        .notificationContent(notificationCreateRequestDTO.getNotificationContent())
                        .notificationType(notificationCreateRequestDTO.getNotificationType())
                        .receiver(receiver)
                        .sender(sender)
                        .isRead(notificationCreateRequestDTO.isRead())
                        .build()
        );

        sendPushNotification(receiver, newNoti.getNotificationType().getValue(), newNoti.getNotificationContent());

        return newNoti;
    }

    @Transactional
    @Override
    public Notification updateNotification(NotificationUpdateRequestDTO notificationUpdateRequestDTO) {

        User sender = null;

        if (notificationUpdateRequestDTO.getSenderId() != null) {
            sender = userRepository.findById(notificationUpdateRequestDTO.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
        }

        User receiver = userRepository.findById(notificationUpdateRequestDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Notification existingNotification = notificationRepository.findById(notificationUpdateRequestDTO.getNotificationId())
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (existingNotification.getSender() != null) {
            existingNotification.getSender().getSentNotifications().remove(existingNotification);
            existingNotification.setSender(sender);
            if (sender != null)
                sender.getSentNotifications().add(existingNotification);
        }

        if (existingNotification.getReceiver() != null) {
            existingNotification.getReceiver().getReceivedNotifications().remove(existingNotification);
            existingNotification.setReceiver(receiver);
            receiver.getReceivedNotifications().add(existingNotification);
        }

        existingNotification.setNotificationContent(notificationUpdateRequestDTO.getNotificationContent());
        existingNotification.setNotificationType(notificationUpdateRequestDTO.getNotificationType());
        existingNotification.setRead(notificationUpdateRequestDTO.isRead());

        return notificationRepository.save(existingNotification);
    }

    @Override
    @Transactional
    public Notification readNotification(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }

        return notification;
    }

    @Override
    @Transactional
    public Page<Notification> sendNotificationToMany(NotificationForManyCreateRequestDTO notificationForManyCreateRequestDTO) {
        List<Notification> returnedNotifications = new ArrayList<>();

        List<User> users = userRepository.findAllById(notificationForManyCreateRequestDTO.getReceiverId());
        User sender = userService.getUser();

        for (User user : users) {
            Notification notification = Notification.builder()
                    .notificationContent(notificationForManyCreateRequestDTO.getNotificationContent())
                    .notificationType(notificationForManyCreateRequestDTO.getNotificationType())
                    .receiver(user)
                    .sender(sender)
                    .build();
            returnedNotifications.add(notificationRepository.save(notification));

            // TODO: Add push notification to receivers
            sendPushNotification(user, notification.getNotificationType().getValue(), notification.getNotificationContent());
        }

        // TODO: Add push notification to sender if applicable
        if (sender != null) {
            sendPushNotification(sender, "Thông báo đã gửi",
                    CreateNotiContentHelper.createManagerNotificationSentContentForMany(users.stream().map(User::getFullName).toList()));
        }

        return new PageImpl<>(returnedNotifications);
    }

    @Override
    @Transactional
    public Page<Notification> sendNotificationToAllUsers(NotificationCreateRequestDTO notificationCreateRequestDTO) {
        List<Notification> returnedNotifications = new ArrayList<>();

        List<User> users = userRepository.findAll();
        User sender = null;
        if (notificationCreateRequestDTO.getSenderId() != null) {
            sender = userRepository.findById(notificationCreateRequestDTO.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
        }
        for (User user : users) {
            Notification notification = Notification.builder()
                    .notificationContent(notificationCreateRequestDTO.getNotificationContent())
                    .notificationType(notificationCreateRequestDTO.getNotificationType())
                    .receiver(user)
                    .sender(sender)
                    .build();
            returnedNotifications.add(notificationRepository.save(notification));
            // TODO: Add push notification to receivers
            sendPushNotification(user, notification.getNotificationType().getValue(), notification.getNotificationContent());
        }

        // TODO: Add push notification to sender if applicable
        if (sender != null) {
            sendPushNotification(sender, "Thông báo đã gửi",
                    CreateNotiContentHelper.createManagerNotificationSentContentForAll());
        }

        return new PageImpl<>(returnedNotifications);
    }

    @Transactional
    @Override
    public void deleteNotification(UUID id) {
        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (existingNotification.getSender() != null) {
            existingNotification.getSender().getSentNotifications().remove(existingNotification);
            existingNotification.setSender(null);
        }

        if (existingNotification.getReceiver() != null) {
            existingNotification.getReceiver().getReceivedNotifications().remove(existingNotification);
            existingNotification.setReceiver(null);
        }

        notificationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addTokenToUser(String token) {
        UUID userId = userService.getUser().getId();
        // check if the token already exists for the user
        UserRecipientToken existingToken = userRecipientTokenRepository.findByFCMRecipientTokenAndUser_Id(token, userId);
        if (existingToken != null) {
            return;
        }
        userRecipientTokenRepository.save(
                UserRecipientToken.builder()
                        .FCMRecipientToken(token)
                        .user(userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException("User not found")))
                        .build()
        );
    }

    @Override
    @Transactional
    public void removeTokenFromUser(String token) {
        User user = userService.getUser();
        UserRecipientToken existingToken = userRecipientTokenRepository.findByFCMRecipientTokenAndUser_Id(token, user.getId());
        if (existingToken == null) {
            return;
        }


        user.getRecipientTokens().remove(existingToken);

        userRecipientTokenRepository.delete(existingToken);
    }

    @Override
    public void sendLoginPushNotification(User user) {
        if (user != null && user.getRecipientTokens() != null && !user.getRecipientTokens().isEmpty()) {
            for (UserRecipientToken token : user.getRecipientTokens()) {
                try {
                    com.google.firebase.messaging.Notification firebaseNotification = com.google.firebase.messaging.Notification.builder()
                            .setTitle("Đăng nhập thành công")
                            .setBody("Chào mừng bạn trở lại, " + user.getFullName() + "!")
                            .build();

                    // Add data payload for handling when app is in background
                    Map<String, String> data = new HashMap<>();
                    data.put("click_action", "OPEN_ACTIVITY");
                    data.put("title", "Đăng nhập thành công");
                    data.put("body", "Chào mừng bạn trở lại, " + user.getFullName() + "!");

                    Message pushNotification = Message.builder()
                            .setToken(token.getFCMRecipientToken())
                            .setNotification(firebaseNotification)
                            .putAllData(data)
                            .setAndroidConfig(AndroidConfig.builder()
                                    .setPriority(AndroidConfig.Priority.HIGH)
                                    .setNotification(AndroidNotification.builder()
                                            .setChannelId("default_channel")
                                            .setSound("default")
                                            .build())
                                    .build())
                            .build();

                    String response = firebaseMessaging.send(pushNotification);
                    System.out.println("Successfully sent notification to: " + token.getFCMRecipientToken());
                    System.out.println("FCM Response: " + response);
                } catch (FirebaseMessagingException e) {
                    System.err.println("Failed to send notification to token: " + token.getFCMRecipientToken());
                    System.err.println("Error code: " + e.getErrorCode() + ", message: " + e.getMessage());

                    // Handle specific error cases
                } catch (Exception e) {
                    System.err.println("Unexpected error sending notification: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No recipient tokens available for user: " + (user != null ? user.getId() : "null"));
        }
    }


    private void sendPushNotification(User user, String title, String body) {
        if (user != null && user.getRecipientTokens() != null && !user.getRecipientTokens().isEmpty()) {
            for (UserRecipientToken token : user.getRecipientTokens()) {
                try {
                    com.google.firebase.messaging.Notification firebaseNotification = com.google.firebase.messaging.Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build();

                    // Add data payload for handling when app is in background
                    Map<String, String> data = new HashMap<>();
                    data.put("click_action", "OPEN_ACTIVITY");
                    data.put("title", title);
                    data.put("body", body);

                    Message pushNotification = Message.builder()
                            .setToken(token.getFCMRecipientToken())
                            .setNotification(firebaseNotification)
                            .putAllData(data)
                            .setAndroidConfig(AndroidConfig.builder()
                                    .setPriority(AndroidConfig.Priority.HIGH)
                                    .setNotification(AndroidNotification.builder()
                                            .setChannelId("default_channel")
                                            .setSound("default")
                                            .build())
                                    .build())
                            .build();

                    String response = firebaseMessaging.send(pushNotification);
                    System.out.println("Successfully sent notification to: " + token.getFCMRecipientToken());
                    System.out.println("FCM Response: " + response);
                } catch (FirebaseMessagingException e) {
                    System.err.println("Failed to send notification to token: " + token.getFCMRecipientToken());
                    System.err.println("Error code: " + e.getErrorCode() + ", message: " + e.getMessage());

                    // Handle specific error cases
                } catch (Exception e) {
                    System.err.println("Unexpected error sending notification: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No recipient tokens available for user: " + (user != null ? user.getId() : "null"));
        }
    }
}
