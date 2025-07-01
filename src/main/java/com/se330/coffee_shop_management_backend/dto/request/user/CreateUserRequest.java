package com.se330.coffee_shop_management_backend.dto.request.user;

import com.se330.coffee_shop_management_backend.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CreateUserRequest extends AbstractBaseCreateUserRequest {
    @NotEmpty(message = "{not_blank}")
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
