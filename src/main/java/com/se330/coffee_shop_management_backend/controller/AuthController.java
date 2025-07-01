package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.auth.LoginRequest;
import com.se330.coffee_shop_management_backend.dto.request.auth.PasswordRequest;
import com.se330.coffee_shop_management_backend.dto.request.auth.RegisterRequest;
import com.se330.coffee_shop_management_backend.dto.request.auth.ResetPasswordRequest;
import com.se330.coffee_shop_management_backend.dto.response.DetailedErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.dto.response.SuccessResponse;
import com.se330.coffee_shop_management_backend.dto.response.auth.PasswordResetResponse;
import com.se330.coffee_shop_management_backend.dto.response.auth.TokenResponse;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.service.AuthService;
import com.se330.coffee_shop_management_backend.service.MessageSourceService;
import com.se330.coffee_shop_management_backend.service.PasswordResetTokenService;
import com.se330.coffee_shop_management_backend.service.UserService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.SECURITY_SCHEME_NAME;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "001. Auth", description = "Auth API")
public class AuthController extends AbstractBaseController {
    private final AuthService authService;

    private final UserService userService;

    private final PasswordResetTokenService passwordResetTokenService;

    private final MessageSourceService messageSourceService;

    private final INotificationService notificationService;

    @PostMapping("/login")
    @Operation(
        summary = "Login endpoint",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Bad credentials",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "422",
                description = "Validation failed",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DetailedErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<SingleResponse<TokenResponse>> login(
        @Parameter(description = "Request body to login", required = true)
        @RequestBody @Validated final LoginRequest request
    ) {
        TokenResponse tokenResponse = authService.login(request.getEmail(), request.getPassword(), false);

        if (request.getFirebaseToken() != null) {
            notificationService.addTokenToUser(request.getFirebaseToken());
        }

        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "login_successful",
                        tokenResponse
                )
        );
    }

    @PostMapping("/register")
    @Operation(
        summary = "Register endpoint",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SuccessResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "422",
                description = "Validation failed",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DetailedErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<SingleResponse<SuccessResponse>> register(
        @Parameter(description = "Request body to register", required = true)
        @RequestBody @Valid RegisterRequest request
    ) throws BindException {
        userService.register(request);

        return ResponseEntity.ok(
            new SingleResponse<>(
                HttpStatus.OK.value(),
                messageSourceService.get("registration_successful"),
                SuccessResponse.builder().message(messageSourceService.get("registered_successfully")).build()
            )
        );
    }

    @GetMapping("/email-verification/{token}")
    @Operation(
        summary = "E-mail verification endpoint",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SuccessResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Not found verification token",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<SingleResponse<SuccessResponse>> emailVerification(
        @Parameter(name = "token", description = "E-mail verification token", required = true)
        @PathVariable("token") final String token
    ) {
        userService.verifyEmail(token);

        return ResponseEntity.ok(
            new SingleResponse<>(
                HttpStatus.OK.value(),
                messageSourceService.get("email_verification_successful"),
                    SuccessResponse.builder()
                            .message(messageSourceService.get("your_email_verified"))
                            .build()
            )
        );
    }

    @GetMapping("/refresh")
    @Operation(
        summary = "Refresh endpoint",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Bad credentials",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<SingleResponse<TokenResponse>> refresh(
        @Parameter(description = "Refresh token", required = true)
        @RequestHeader("Authorization") @Validated final String refreshToken
    ) {
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        messageSourceService.get("registration_successful"),
                        authService.refreshFromBearerString(refreshToken)
                )
        );
    }

    @PostMapping("/reset-password")
    @Operation(
        summary = "Reset password endpoint",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SuccessResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Bad credentials",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<SingleResponse<SuccessResponse>> resetPassword(
        @Parameter(description = "Request body to password", required = true)
        @RequestBody @Valid PasswordRequest request
    ) {
        authService.resetPassword(request.getEmail());

        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        messageSourceService.get("registration_successful"),
                        SuccessResponse.builder()
                                .message(messageSourceService.get("password_reset_link_sent"))
                                .build()
                )
        );
    }

    @GetMapping("/reset-password/{token}")
    @Operation(
        summary = "Reset password check token endpoint",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PasswordResetResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Bad credentials",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<SingleResponse<PasswordResetResponse>> resetPassword(
        @Parameter(name = "token", description = "Password reset token", required = true)
        @PathVariable("token") final String token
    ) {
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        messageSourceService.get("registration_successful"),
                        PasswordResetResponse.convert(passwordResetTokenService.findByToken(token))
                )
        );
    }

    @PostMapping("/reset-password/{token}")
    @Operation(
        summary = "Reset password with token endpoint",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PasswordResetResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Bad credentials",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<SingleResponse<SuccessResponse>> resetPassword(
        @Parameter(name = "token", description = "Password reset token", required = true)
        @PathVariable("token") final String token,
        @Parameter(description = "Request body to update password", required = true)
        @RequestBody @Valid ResetPasswordRequest request
    ) {
        userService.resetPassword(token, request);

        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        messageSourceService.get("registration_successful"),
                        SuccessResponse.builder()
                                .message(messageSourceService.get("password_reset_success_successfully"))
                                .build()
                )
        );
    }

    @GetMapping("/logout")
    @Operation(
        summary = "Logout endpoint",
        security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SuccessResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Bad request",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
        }
    )
    public ResponseEntity<SingleResponse<SuccessResponse>> logout(
            @Parameter(description = "Firebase token (optional)")
            @RequestParam(required = false) String firebaseToken
    ) {

        User user = userService.getUser();

        authService.logout(user);

        if (firebaseToken != null) {
            notificationService.removeTokenFromUser(firebaseToken);
        }

        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        messageSourceService.get("registration_successful"),
                        SuccessResponse.builder()
                                .message(messageSourceService.get("logout_successfully"))
                                .build()
                )
        );
    }
}
