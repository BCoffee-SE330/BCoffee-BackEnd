package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class UserAdminResponse {
    // Mã người dùng
    private String userId;
    // Email của người dùng
    private String email;
    // Mật khẩu của người dùng
    private String password;
    // Tên của người dùng
    private String name;
    // Họ của người dùng
    private String lastName;
    // Ảnh đại diện của người dùng
    private String avatar;
    // Số điện thoại của người dùng
    private String phoneNumber;
    // Giới tính của người dùng
    private String gender;
    // Ngày sinh của người dùng
    private LocalDateTime birthDate;
    // Mã vai trò của người dùng, khoá ngoại tham chiếu đến bảng vai trò
    private String roleId;
    // Ngày tạo người dùng
    private LocalDateTime createdAt;
    // Thời gian cập nhật người dùng gần nhất
    private LocalDateTime updatedAt;

    public static UserAdminResponse convert(User user) {
        return UserAdminResponse.builder()
                .userId(user.getId().toString())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .lastName(user.getLastName())
                .avatar(user.getAvatar())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .roleId(user.getRole() != null ? user.getRole().getId().toString() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static List<UserAdminResponse> convert(List<User> users) {
        if (users == null || users.isEmpty()) {
            return List.of();
        }
        return users.stream()
                .map(UserAdminResponse::convert)
                .toList();
    }
}
