package com.se330.coffee_shop_management_backend.service;

import com.se330.coffee_shop_management_backend.dto.response.auth.TokenResponse;
import com.se330.coffee_shop_management_backend.entity.JwtToken;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.exception.NotFoundException;
import com.se330.coffee_shop_management_backend.exception.RefreshTokenExpiredException;
import com.se330.coffee_shop_management_backend.security.JwtTokenProvider;
import com.se330.coffee_shop_management_backend.security.JwtUserDetails;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.TOKEN_HEADER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;

    private final JwtTokenService jwtTokenService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final HttpServletRequest httpServletRequest;

    private final MessageSourceService messageSourceService;

    private final INotificationService notificationService;

    /**
     * Authenticate user.
     *
     * @param email      String
     * @param password   String
     * @param rememberMe Boolean
     * @return TokenResponse
     */
    @Transactional
    public TokenResponse login(String email, final String password, final Boolean rememberMe) {
        log.info("Login request received: {}", email);

        String badCredentialsMessage = messageSourceService.get("bad_credentials");

        User user = null;
        try {
            user = userService.findByEmail(email);
            email = user.getEmail();
        } catch (NotFoundException e) {
            log.error("User not found with email: {}", email);
            throw new AuthenticationCredentialsNotFoundException(badCredentialsMessage);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(email, password);
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            JwtUserDetails jwtUserDetails = jwtTokenProvider.getPrincipal(authentication);

            notificationService.sendLoginPushNotification(user);

            return generateTokens(UUID.fromString(jwtUserDetails.getId()), rememberMe);
        } catch (NotFoundException e) {
            log.error("Authentication failed for email: {}", email);
            throw new AuthenticationCredentialsNotFoundException(badCredentialsMessage);
        }
    }

    /**
     * Refresh from bearer string.
     *
     * @param bearer String
     * @return TokenResponse
     */
    @Transactional
    public TokenResponse refreshFromBearerString(final String bearer) {
        return refresh(jwtTokenProvider.extractJwtFromBearerString(bearer));
    }

    /**
     * Reset password by e-mail.
     *
     * @param email String
     */
    @Transactional
    public void resetPassword(String email) {
        log.info("Reset password request received: {}", email);
        userService.sendEmailPasswordResetMail(email);
    }

    /**
     * Logout from bearer string by user.
     *
     * @param user   User
     * @param bearer String
     */
    @Transactional
    public void logout(User user, final String bearer) {
        JwtToken jwtToken = jwtTokenService.findByTokenOrRefreshToken(
            jwtTokenProvider.extractJwtFromBearerString(bearer));

        if (!user.getId().equals(jwtToken.getUserId())) {
            log.error("User id: {} is not equal to token user id: {}", user.getId(), jwtToken.getUserId());
            throw new AuthenticationCredentialsNotFoundException(messageSourceService.get("bad_credentials"));
        }

        jwtTokenService.delete(jwtToken);
    }

    /**
     * Logout from bearer string by user.
     *
     * @param user User
     */
    @Transactional
    public void logout(User user) {
        logout(user, httpServletRequest.getHeader(TOKEN_HEADER));
    }

    /**
     * Refresh token.
     *
     * @param refreshToken String
     * @return TokenResponse
     */
    @Transactional
    public TokenResponse refresh(final String refreshToken) {
        log.info("Refresh request received: {}", refreshToken);

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            log.error("Refresh token is expired.");
            throw new RefreshTokenExpiredException();
        }

        User user = jwtTokenProvider.getUserFromToken(refreshToken);
        JwtToken oldToken = jwtTokenService.findByUserIdAndRefreshToken(user.getId(), refreshToken);
        if (oldToken != null && oldToken.getRememberMe()) {
            jwtTokenProvider.setRememberMe();
        }

        boolean rememberMe = false;
        if (oldToken != null) {
            rememberMe = oldToken.getRememberMe();
            jwtTokenService.delete(oldToken);
        }

        return generateTokens(user.getId(), rememberMe);
    }

    /**
     * Generate both access and refresh tokens.
     *
     * @param id         user identifier to set the subject for token and value for the expiring map
     * @param rememberMe Boolean option to set the expiration time for refresh token
     * @return an object of TokenResponse
     */
    @Transactional
    public TokenResponse generateTokens(final UUID id, final Boolean rememberMe) {
        String token = jwtTokenProvider.generateJwt(id.toString());
        String refreshToken = jwtTokenProvider.generateRefresh(id.toString());
        if (rememberMe) {
            jwtTokenProvider.setRememberMe();
        }

        jwtTokenService.save(JwtToken.builder()
            .userId(id)
            .token(token)
            .refreshToken(refreshToken)
            .rememberMe(rememberMe)
            .ipAddress(httpServletRequest.getRemoteAddr())
            .userAgent(httpServletRequest.getHeader("User-Agent"))
            .tokenTimeToLive(jwtTokenProvider.getRefreshTokenExpiresIn())
            .build());
        log.info("Token generated for user: {}", id);

        User user = userService.findById(id);
        String role = user.getRole().getName().getValue();

        return TokenResponse.builder()
            .accessToken(token)
            .refreshToken(refreshToken)
            .id(String.valueOf(id))
            .role(role)
            .build();
    }
}
