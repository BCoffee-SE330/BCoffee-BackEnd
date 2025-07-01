package com.se330.coffee_shop_management_backend.dto.response.shift;

import com.se330.coffee_shop_management_backend.entity.Shift;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@SuperBuilder
public class ShiftResponseDTO {

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


    public static ShiftResponseDTO convert(Shift shift) {
        return ShiftResponseDTO.builder()
                .id(shift.getId().toString())
                .createdAt(shift.getCreatedAt())
                .updatedAt(shift.getUpdatedAt())
                .month(shift.getMonth())
                .year(shift.getYear())
                .shiftStartTime(shift.getShiftStartTime())
                .shiftEndTime(shift.getShiftEndTime())
                .dayOfWeek(shift.getDayOfWeek().getValue())
                .employeeId(shift.getEmployee() != null ? shift.getEmployee().getId().toString() : null)
                .employeeFullName(shift.getEmployee() != null ? shift.getEmployee().getUser().getFullName() : null)
                .employeeAvatarUrl(shift.getEmployee() != null ? shift.getEmployee().getUser().getAvatar() : null)
                .build();
    }

    public static List<ShiftResponseDTO> convert(List<Shift> shifts) {
        if (shifts == null || shifts.isEmpty()) {
            return Collections.emptyList();
        }

        return shifts.stream()
                .map(ShiftResponseDTO::convert)
                .collect(Collectors.toList());
    }
}