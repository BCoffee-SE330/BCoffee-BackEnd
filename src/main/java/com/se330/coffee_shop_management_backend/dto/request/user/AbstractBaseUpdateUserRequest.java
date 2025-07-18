package com.se330.coffee_shop_management_backend.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractBaseUpdateUserRequest {
    @Email(message = "{invalid_email}")
    @Size(max = 100, message = "{max_length}")
    @Schema(
        name = "email",
        description = "Email of the user",
        type = "String",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        example = "mail@example.com"
    )
    private String email;

    @Size(max = 50, message = "{max_length}")
    @Schema(
        name = "name",
        description = "Name of the user",
        type = "String",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        example = "John"
    )
    private String name;

    @Size(max = 50, message = "{max_length}")
    @Schema(
        name = "lastName",
        description = "Lastname of the user",
        type = "String",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        example = "DOE"
    )
    private String lastName;

    private String phoneNumber;
    private String gender;
    private String birthDate;
}
