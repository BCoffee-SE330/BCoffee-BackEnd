package com.se330.coffee_shop_management_backend.entity;

import com.se330.coffee_shop_management_backend.util.Constants;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "notification_id"))
})
public class Notification extends AbstractBaseEntity {
    @Column(name = "notification_type", nullable = false)
    private Constants.NotificationTypeEnum notificationType;

    @Column(name = "notification_content")
    private String notificationContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_sender_id",
            foreignKey = @ForeignKey(
                    name = "fk_notification_sender",
                    foreignKeyDefinition = "FOREIGN KEY (user_sender_id) REFERENCES users (id) ON DELETE CASCADE"
            )
    )
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_receiver_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_notification_receiver",
                    foreignKeyDefinition = "FOREIGN KEY (user_receiver_id) REFERENCES users (id) ON DELETE CASCADE"
            )
    )
    private User receiver;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;
}