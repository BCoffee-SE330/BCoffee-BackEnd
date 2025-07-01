package com.se330.coffee_shop_management_backend.dto.response.shift;

import com.se330.coffee_shop_management_backend.entity.Shift;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@SuperBuilder
public class ShiftIsCheckinResponseDTO {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;
    private String dayOfWeek;
    private int month;
    private int year;
    private BigDecimal shiftSalary;

    @Schema(
            name = "createdAt",
            description = "Date time field of shift creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of shift update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;



    // Related entities
    private String employeeId;
    private String employeeFullName;
    private String employeeAvatarUrl;
    private boolean isCheckin;

    public static ShiftIsCheckinResponseDTO convert(Shift shift, boolean isCheckin) {
        return ShiftIsCheckinResponseDTO.builder()
                .id(String.valueOf(shift.getId()))
                .createdAt(shift.getCreatedAt())
                .updatedAt(shift.getUpdatedAt())
                .shiftStartTime(shift.getShiftStartTime())
                .shiftEndTime(shift.getShiftEndTime())
                .dayOfWeek(shift.getDayOfWeek().getValue())
                .month(shift.getMonth())
                .year(shift.getYear())
                .shiftSalary(shift.getShiftSalary())
                .employeeId(shift.getEmployee().getId().toString())
                .employeeFullName(shift.getEmployee().getUser().getFullName())
                .employeeAvatarUrl(shift.getEmployee().getUser().getAvatar())
                .isCheckin(isCheckin)
                .build();
    }

    public static List<ShiftIsCheckinResponseDTO> convert(Map<Shift, Boolean> shiftAndCheckin) {
        if (shiftAndCheckin == null || shiftAndCheckin.isEmpty()) {
            return List.of();
        }

        return shiftAndCheckin.entrySet().stream()
                .map(entry -> convert(entry.getKey(), entry.getValue()))
                .toList();
    }
}
