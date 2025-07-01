package com.se330.coffee_shop_management_backend.service;

import com.se330.coffee_shop_management_backend.dto.request.auth.RegisterRequest;
import com.se330.coffee_shop_management_backend.dto.request.auth.ResetPasswordRequest;
import com.se330.coffee_shop_management_backend.dto.request.user.AbstractBaseCreateUserRequest;
import com.se330.coffee_shop_management_backend.dto.request.user.AbstractBaseUpdateUserRequest;
import com.se330.coffee_shop_management_backend.dto.request.user.CreateUserRequest;
import com.se330.coffee_shop_management_backend.dto.request.user.UpdatePasswordRequest;
import com.se330.coffee_shop_management_backend.dto.request.user.UpdateUserRequest;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.entity.specification.UserFilterSpecification;
import com.se330.coffee_shop_management_backend.entity.specification.criteria.PaginationCriteria;
import com.se330.coffee_shop_management_backend.entity.specification.criteria.UserCriteria;
import com.se330.coffee_shop_management_backend.event.UserEmailVerificationSendEvent;
import com.se330.coffee_shop_management_backend.event.UserPasswordResetSendEvent;
import com.se330.coffee_shop_management_backend.exception.BadRequestException;
import com.se330.coffee_shop_management_backend.exception.NotFoundException;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.security.JwtUserDetails;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.PageRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final EmailVerificationTokenService emailVerificationTokenService;

    private final PasswordResetTokenService passwordResetTokenService;

    private final ApplicationEventPublisher eventPublisher;

    private final MessageSourceService messageSourceService;

    private final CloudinaryService cloudinaryService;

    /**
     * Get authentication.
     *
     * @return Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Return the authenticated user.
     *
     * @return user User
     */
    @Transactional(readOnly = true)
    public User getUser() {
        Authentication authentication = getAuthentication();
        if (authentication.isAuthenticated()) {
            try {
                return findById(getPrincipal(authentication).getId());
            } catch (ClassCastException | NotFoundException e) {
                log.warn("[JWT] User details not found!");
                throw new BadCredentialsException(messageSourceService.get("bad_credentials"));
            }
        } else {
            log.warn("[JWT] User not authenticated!");
            throw new BadCredentialsException(messageSourceService.get("bad_credentials"));
        }
    }

    /**
     * Count users.
     *
     * @return Long
     */
    @Transactional(readOnly = true)
    public long count() {
        return userRepository.count();
    }

    /**
     * Find all users with pagination.
     *
     * @param criteria           UserCriteria
     * @param paginationCriteria PaginationCriteria
     * @return Page
     */
    @Transactional(readOnly = true)
    public Page<User> findAll(UserCriteria criteria, PaginationCriteria paginationCriteria) {
        return userRepository.findAll(new UserFilterSpecification(criteria),
            PageRequestBuilder.build(paginationCriteria));
    }

    /**
     * Find a user by id.
     *
     * @param id UUID
     * @return User
     */
    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                new String[]{messageSourceService.get("user")})));
    }

    /**
     * Find a user by id.
     *
     * @param id String
     * @return User
     */
    @Transactional(readOnly = true)
    public User findById(String id) {
        return findById(UUID.fromString(id));
    }

    /**
     * Find a user by email.
     *
     * @param email String.
     * @return User
     */
    @Transactional(readOnly = true)
    public User findByEmail(final String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                new String[]{messageSourceService.get("user")})));
    }

    /**
     * Load user details by username.
     *
     * @param email String
     * @return UserDetails
     * @throws UsernameNotFoundException email not found exception.
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserByEmail(final String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                new String[]{messageSourceService.get("user")})));

        return JwtUserDetails.create(user);
    }

    /**
     * Loads user details by UUID string.
     *
     * @param id String
     * @return UserDetails
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(final String id) {
        User user = userRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                new String[]{messageSourceService.get("user")})));

        return JwtUserDetails.create(user);
    }

    /**
     * Get UserDetails from security context.
     *
     * @param authentication Wrapper for security context
     * @return the Principal being authenticated or the authenticated principal after authentication.
     */
    @Transactional(readOnly = true)
    public JwtUserDetails getPrincipal(final Authentication authentication) {
        return (JwtUserDetails) authentication.getPrincipal();
    }

    /**
     * Register user.
     *
     * @param request RegisterRequest
     * @return User
     */
    @Transactional
    public User register(final RegisterRequest request) throws BindException {
        log.info("Registering user with email: {}", request.getEmail());

        User user = createUser(request);
        user.setRole(roleService.findByName(Constants.RoleEnum.CUSTOMER));
        userRepository.save(user);

        emailVerificationEventPublisher(user);

        log.info("User registered with email: {}, {}", user.getEmail(), user.getId());

        return user;
    }

    /**
     * Create user.
     *
     * @param request CreateUserRequest
     * @return User
     */
    @Transactional
    public User create(final CreateUserRequest request) throws BindException {
        log.info("Creating user with email: {}", request.getEmail());

        User user = createUser(request);
        user.setRole(roleService.findByName(request.getRole()));

        if (request.getIsEmailVerified() != null && request.getIsEmailVerified()) {
            user.setEmailVerifiedAt(LocalDateTime.now());
        }

        if (request.getIsBlocked() != null && request.getIsBlocked()) {
            user.setBlockedAt(LocalDateTime.now());
        }

        userRepository.save(user);

        log.info("User created with email: {}, {}", user.getEmail(), user.getId());

        return user;
    }

    /**
     * Update user.
     *
     * @param id      UUID
     * @param request UpdateUserRequest
     * @return User
     */
    @Transactional
    public User update(UUID id, UpdateUserRequest request) throws BindException {
        User user = findById(id);
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setLastName(request.getLastName());

        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRole() != null) {
            user.setRole(roleService.findByName(request.getRole()));
        }

        if (request.getIsEmailVerified() != null) {
            if (request.getIsEmailVerified()) {
                user.setEmailVerifiedAt(LocalDateTime.now());
            } else {
                user.setEmailVerifiedAt(null);
            }
        }

        return updateUser(user, request);
    }

    /**
     * Update user.
     *
     * @param id      String
     * @param request UpdateUserRequest
     * @return User
     */
    @Transactional
    public User update(String id, UpdateUserRequest request) throws BindException {
        return update(UUID.fromString(id), request);
    }

    /**
     * Update user password.
     *
     * @param request UpdatePasswordRequest
     */
    @Transactional
    public User updatePassword(UpdatePasswordRequest request) throws BindException {
        User user = getUser();
        log.info("Updating password for user with email: {}", user.getEmail());

        BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "oldPassword",
                messageSourceService.get("invalid_old_password")));
        }

        if (request.getOldPassword().equals(request.getPassword())) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "password",
                messageSourceService.get("same_password_error")));
        }

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        log.info("Password updated for user with email: {}", user.getEmail());

        return user;
    }

    /**
     * Reset password.
     *
     * @param token String
     * @param request ResetPasswordRequest
     */
    @Transactional
    public void resetPassword(String token, ResetPasswordRequest request) {
        User user = passwordResetTokenService.getUserByToken(token);
        log.info("Resetting password for user with email: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        passwordResetTokenService.deleteByUserId(user.getId());
        log.info("Password reset for user with email: {}", user.getEmail());
    }

    /**
     * Resend e-mail verification mail.
     */
    @Transactional
    public void resendEmailVerificationMail() {
        User user = getUser();
        log.info("Resending e-mail verification mail to email: {}", user.getEmail());
        if (user.getEmailVerifiedAt() != null) {
            throw new BadRequestException(messageSourceService.get("your_email_already_verified"));
        }

        emailVerificationEventPublisher(user);
        log.info("Resending e-mail verification mail to email: {}", user.getEmail());
    }

    /**
     * E-mail verification.
     *
     * @param token String
     */
    @Transactional
    public void verifyEmail(String token) {
        log.info("Verifying e-mail with token: {}", token);
        User user = emailVerificationTokenService.getUserByToken(token);
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        emailVerificationTokenService.deleteByUserId(user.getId());
        log.info("E-mail verified with token: {}", token);
    }

    /**
     * Send password reset mail.
     *
     * @param email String
     */
    @Transactional
    public void sendEmailPasswordResetMail(String email) {
        log.info("Sending password reset mail to email: {}", email);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                new String[]{messageSourceService.get("user")})));

        passwordResetEventPublisher(user);
        log.info("Password reset mail sent to email: {}", email);
    }

    /**
     * Delete user.
     *
     * @param id UUID
     */
    @Transactional
    public void delete(String id) {
        userRepository.delete(findById(id));
    }

    /**
     * Create user.
     *
     * @param request AbstractBaseCreateUserRequest
     * @return User
     */
    @Transactional
    protected User createUser(AbstractBaseCreateUserRequest request) throws BindException {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    log.error("User with email: {} already exists", request.getEmail());
                    bindingResult.addError(new FieldError(bindingResult.getObjectName(), "email",
                            messageSourceService.get("unique_email")));
                });

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .birthDate(request.getBirthDate())
                .avatar(cloudinaryService.getAvatarDefault())
                .build();
    }

    /**
     * Update user.
     *
     * @param user    User
     * @param request UpdateUserRequest
     * @return User
     */
    @Transactional
    protected User updateUser(User user, AbstractBaseUpdateUserRequest request) throws BindException {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "request");
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmailAndIdNot(request.getEmail(), user.getId())) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "email",
                    messageSourceService.get("already_exists")));
        }

        boolean isRequiredEmailVerification = false;
        if (StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(user.getEmail())) {
            user.setEmail(request.getEmail());
            user.setEmailVerificationToken(emailVerificationTokenService.create(user));
            isRequiredEmailVerification = true;
        }

        if (StringUtils.hasText(request.getName()) && !request.getName().equals(user.getName())) {
            user.setName(request.getName());
        }

        if (StringUtils.hasText(request.getLastName()) && !request.getLastName().equals(user.getLastName())) {
            user.setLastName(request.getLastName());
        }

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        userRepository.save(user);

        if (isRequiredEmailVerification) {
            emailVerificationEventPublisher(user);
        }

        return user;
    }

    /**
     * E-mail verification event publisher.
     *
     * @param user User
     */
    @Transactional
    protected void emailVerificationEventPublisher(User user) {
        user.setEmailVerificationToken(emailVerificationTokenService.create(user));
        eventPublisher.publishEvent(new UserEmailVerificationSendEvent(this, user));
    }

    /**
     * Password reset event publisher.
     *
     * @param user User
     */
    @Transactional
    protected void passwordResetEventPublisher(User user) {
        user.setPasswordResetToken(passwordResetTokenService.create(user));
        eventPublisher.publishEvent(new UserPasswordResetSendEvent(this, user));
    }

    /**
     * Upload user avatar image.
     *
     * @param userId UUID of the user
     * @param file MultipartFile image to upload
     * @return String URL of the uploaded image
     * @throws Exception if there's an error during upload
     */
    @Transactional
    public String uploadUserAvatar(UUID userId, MultipartFile file) throws Exception {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                        new String[]{messageSourceService.get("user")})));

        // Check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        // Check if the current avatar is the default image
        if (existingUser.getAvatar().equals(cloudinaryService.getAvatarDefault())) {
            // Upload the new image
            Map uploadResult = cloudinaryService.uploadFile(file, "avatars");
            String imageUrl = uploadResult.get("secure_url").toString();
            // Update the user's avatar URL
            existingUser.setAvatar(imageUrl);
            userRepository.save(existingUser);

            return imageUrl;
        } else {
            // Delete the old image from Cloudinary
            try {
                cloudinaryService.deleteFile(existingUser.getAvatar());
            } catch (IOException e) {
                log.error("Failed to delete old user avatar: {}", e.getMessage());
                // Continue with upload even if delete fails
            }

            // Upload the new image
            Map uploadResult = cloudinaryService.uploadFile(file, "avatars");
            String imageUrl = uploadResult.get("secure_url").toString();

            // Update the user's avatar URL
            existingUser.setAvatar(imageUrl);
            userRepository.save(existingUser);

            return imageUrl;
        }
    }

    /**
     * Delete the user avatar and set back to default.
     *
     * @param userId UUID of the user
     * @return String URL of the default avatar
     * @throws Exception if there's an error during deletion
     */
    @Transactional
    public String deleteUserAvatar(UUID userId) throws Exception {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(messageSourceService.get("not_found_with_param",
                        new String[]{messageSourceService.get("user")})));

        String defaultAvatarUrl = cloudinaryService.getAvatarDefault();

        // If the current avatar is already the default, no need to do anything
        if (existingUser.getAvatar().equals(defaultAvatarUrl)) {
            return defaultAvatarUrl;
        }

        // Delete the current avatar from Cloudinary
        try {
            cloudinaryService.deleteFile(existingUser.getAvatar());
        } catch (IOException e) {
            log.error("Failed to delete user avatar: {}", e.getMessage());
            // Continue even if delete fails
        }

        // Set the user's avatar back to the default
        existingUser.setAvatar(defaultAvatarUrl);
        userRepository.save(existingUser);

        return defaultAvatarUrl;
    }
}
