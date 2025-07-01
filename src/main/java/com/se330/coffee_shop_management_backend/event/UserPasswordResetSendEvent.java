package com.se330.coffee_shop_management_backend.event;

import com.se330.coffee_shop_management_backend.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserPasswordResetSendEvent extends ApplicationEvent {
    private final User user;

    public UserPasswordResetSendEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
