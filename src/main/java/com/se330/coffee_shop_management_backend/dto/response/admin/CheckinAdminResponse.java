package com.se330.coffee_shop_management_backend.dto.response.admin;

import com.se330.coffee_shop_management_backend.entity.Checkin;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class CheckinAdminResponse {
    // Mã của lượt chấm công
    private String id;
    // Thời gian tạo chấm công
    private LocalDateTime createdAt;
    // Thời gian cập nhật chấm công gần nhất
    private LocalDateTime updatedAt;
    // Thời gian chấm công
    private LocalDateTime checkinTime;
    // Mã của ca làm việc, tham chiếu đến bảng Shift
    private String shiftId;

    public static CheckinAdminResponse convert(Checkin checkin) {
        return CheckinAdminResponse.builder()
                .id(checkin.getId().toString())
                .createdAt(checkin.getCreatedAt())
                .updatedAt(checkin.getUpdatedAt())
                .checkinTime(checkin.getCheckinTime())
                .shiftId(checkin.getShift() != null ? checkin.getShift().getId().toString() : null)
                .build();
    }

    public static List<CheckinAdminResponse> convert(List<Checkin> checkins) {
        if (checkins == null || checkins.isEmpty()) {
            return List.of();
        }
        return checkins.stream()
                .map(CheckinAdminResponse::convert)
                .toList();
    }
}