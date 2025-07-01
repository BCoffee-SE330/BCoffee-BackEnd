package com.se330.coffee_shop_management_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_recipient_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "user_recipient_token_id"))
})
public class UserRecipientToken extends AbstractBaseEntity {

    @Column(name = "recipient_token", nullable = false, columnDefinition = "text")
    private String FCMRecipientToken;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(
                    name = "fk_user_recipient_tokens_user_id",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users (id)"
            )
    )
    private User user;
}