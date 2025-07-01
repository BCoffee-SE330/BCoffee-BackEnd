package com.se330.coffee_shop_management_backend.dto.response.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class SubCheckinAdminResponse {
    // Mã định danh của lượt chấm công
    private String shiftId;
    // Mã định danh của nhân viên chấm công thay thế, khóa ngoại tham chiếu đến bảng nhân viên
    private String employeeId;
    // Thời gian chấm công thay thế
    private LocalDateTime checkinTime;

    public static SubCheckinAdminResponse convert(com.se330.coffee_shop_management_backend.entity.SubCheckin subCheckin) {
        return SubCheckinAdminResponse.builder()
                .shiftId(subCheckin.getShift() != null ? subCheckin.getShift().getId().toString() : null)
                .employeeId(subCheckin.getEmployee() != null ? subCheckin.getEmployee().getId().toString() : null)
                .checkinTime(subCheckin.getCheckinTime())
                .build();
    }

    public static java.util.List<SubCheckinAdminResponse> convert(java.util.List<com.se330.coffee_shop_management_backend.entity.SubCheckin> subCheckins) {
        if (subCheckins == null || subCheckins.isEmpty()) {
            return java.util.List.of();
        }
        return subCheckins.stream()
                .map(SubCheckinAdminResponse::convert)
                .toList();
    }
}
