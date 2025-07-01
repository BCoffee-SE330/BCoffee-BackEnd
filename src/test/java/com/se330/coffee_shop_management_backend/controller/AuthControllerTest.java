package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.dto.request.auth.LoginRequest;
import com.se330.coffee_shop_management_backend.dto.request.auth.PasswordRequest;
import com.se330.coffee_shop_management_backend.dto.request.auth.RegisterRequest;
import com.se330.coffee_shop_management_backend.dto.request.auth.ResetPasswordRequest;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.dto.response.SuccessResponse;
import com.se330.coffee_shop_management_backend.dto.response.auth.PasswordResetResponse;
import com.se330.coffee_shop_management_backend.dto.response.auth.TokenResponse;
import com.se330.coffee_shop_management_backend.entity.PasswordResetToken;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.service.AuthService;
import com.se330.coffee_shop_management_backend.service.MessageSourceService;
import com.se330.coffee_shop_management_backend.service.PasswordResetTokenService;
import com.se330.coffee_shop_management_backend.service.UserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for AuthController")
class AuthControllerTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordResetTokenService passwordResetTokenService;

    @Mock
    private MessageSourceService messageSourceService;

    private final LoginRequest loginRequest = Instancio.create(LoginRequest.class);

    private final TokenResponse tokenResponse = Instancio.create(TokenResponse.class);

    private final User user = Instancio.create(User.class);

    @Test
    @DisplayName("Test for login")
    void given_whenLogin_thenAssertBody() {
        // Given
        when(authService.login(loginRequest.getEmail(), loginRequest.getPassword(), false))
            .thenReturn(tokenResponse);
        // When
        ResponseEntity<SingleResponse<TokenResponse>> response = authController.login(loginRequest);
        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(tokenResponse, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test for register")
    void given_whenRegister_thenAssertBody() throws BindException {
        // Given
        String message = "registered_successfully";
        RegisterRequest loginRequest = Instancio.create(RegisterRequest.class);
        when(userService.register(loginRequest)).thenReturn(user);
        when(messageSourceService.get(message)).thenReturn(message);
        // When
        ResponseEntity<SingleResponse<SuccessResponse>> response = authController.register(loginRequest);
        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(message, response.getBody().getMessage());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test for email verification")
    void given_whenEmailVerification_thenAssertBody() {
        // Given
        String token = "token";
        String message = "your_email_verified";
        doNothing().when(userService).verifyEmail(token);
        when(messageSourceService.get(message)).thenReturn(message);
        // When
        ResponseEntity<SingleResponse<SuccessResponse>> response = authController.emailVerification(token);
        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(message, response.getBody().getMessage());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test for refresh")
    void given_whenRefresh_thenAssertBody() {
        // Given
        String refreshToken = "refreshToken";
        when(authService.refreshFromBearerString("refreshToken")).thenReturn(tokenResponse);
        // When
        ResponseEntity<SingleResponse<TokenResponse>> response = authController.refresh(refreshToken);
        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(tokenResponse, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test for reset password")
    void given_whenResetPassword_thenAssertBody() {
        // Given
        String message = "Password reset successfully";
        PasswordRequest request = Instancio.create(PasswordRequest.class);
        doNothing().when(authService).resetPassword(request.getEmail());
        when(messageSourceService.get("password_reset_link_sent")).thenReturn(message);
        // When
        ResponseEntity<SingleResponse<SuccessResponse>> response = authController.resetPassword(request);
        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody().getMessage());
    }

    @Test
    @DisplayName("Test for check token for reset password")
    void given_whenResetPasswordCheckToken_thenAssertBody() {
        // Given
        String token = "token";
        PasswordResetToken passwordResetToken = Instancio.create(PasswordResetToken.class);
        when(passwordResetTokenService.findByToken(token)).thenReturn(passwordResetToken);
        // When
        ResponseEntity<SingleResponse<PasswordResetResponse>> response = authController.resetPassword(token);
        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passwordResetToken.getToken(), response.getBody().getData().getToken());
        assertEquals(passwordResetToken.getUser().getEmail(), response.getBody().getData().getUser().getEmail());
    }

    @Test
    @DisplayName("Test for reset password with token")
    void given_whenResetPasswordWithToken_thenAssertBody() {
        // Given
        String token = "token";
        String message = "Password reset success successfully";
        ResetPasswordRequest request = Instancio.create(ResetPasswordRequest.class);
        when(messageSourceService.get("password_reset_success_successfully")).thenReturn(message);
        doNothing().when(userService).resetPassword(token, request);
        // When
        ResponseEntity<SingleResponse<SuccessResponse>> response = authController.resetPassword(token, request);
        // Then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody().getMessage());
    }

    @Test
    @DisplayName("Test for forgot logout")
    void given_whenLogout_thenAssertBody() {
        // Given
        when(userService.getUser()).thenReturn(user);
        doNothing().when(authService).logout(user);
        when(messageSourceService.get("logout_successfully")).thenReturn("successfully");
        // When
        //ResponseEntity<SingleResponse<SuccessResponse>> response = authController.logout();
        // Then
//        assertNotNull(response);
//        assertNotNull(response.getBody());
//        assertEquals("successfully", response.getBody().getMessage());
//        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
