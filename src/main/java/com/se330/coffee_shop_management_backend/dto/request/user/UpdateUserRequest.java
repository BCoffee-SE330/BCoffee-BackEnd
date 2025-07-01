package com.se330.coffee_shop_management_backend.dto.request.user;

import com.se330.coffee_shop_management_backend.dto.annotation.FieldMatch;
import com.se330.coffee_shop_management_backend.dto.annotation.Password;
import com.se330.coffee_shop_management_backend.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@FieldMatch(first = "password", second = "passwordConfirm", message = "{password_mismatch}")
public class UpdateUserRequest extends AbstractBaseUpdateUserRequest {
    @Password(message = "{invalid_password}")
    @Schema(
        name = "password",
        description = "Password of the user",
        type = "String",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        example = "P@sswd123."
    )
    private String password;

    @Schema(
        name = "passwordConfirm",
        description = "Password for confirmation",
        type = "String",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        example = "P@sswd123."
    )
    private String passwordConfirm;

    private Constants.RoleEnum role;

    @Schema(
        name = "isEmailVerified",
        description = "Is the user's email verified",
        type = "Boolean",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        example = "true"
    )
    @Builder.Default
    private Boolean isEmailVerified = false;

    @Schema(
        name = "isBlocked",
        description = "Is the user blocked",
        type = "Boolean",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        example = "false"
    )
    @Builder.Default
    private Boolean isBlocked = false;
}
