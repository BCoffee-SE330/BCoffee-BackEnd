package com.se330.coffee_shop_management_backend.dto.request.auth;

import com.se330.coffee_shop_management_backend.dto.request.user.AbstractBaseCreateUserRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class RegisterRequest extends AbstractBaseCreateUserRequest {
}
