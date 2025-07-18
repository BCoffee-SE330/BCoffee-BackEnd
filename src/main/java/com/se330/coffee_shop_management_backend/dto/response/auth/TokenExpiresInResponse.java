package com.se330.coffee_shop_management_backend.dto.response.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TokenExpiresInResponse {
    @Schema(
        name = "token",
        description = "Token expires In",
        type = "Long",
        example = "3600"
    )
    private Long token;

    @Schema(
        name = "refreshToken",
        description = "Refresh token expires In",
        type = "Long",
        example = "3600"
    )
    private Long refreshToken;
}
