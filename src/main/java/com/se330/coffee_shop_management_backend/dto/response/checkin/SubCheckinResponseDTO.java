package com.se330.coffee_shop_management_backend.dto.response.checkin;

import com.se330.coffee_shop_management_backend.entity.SubCheckin;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class SubCheckinResponseDTO {
    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    @Schema(
            name = "createdAt",
            description = "Date time field of checkin creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of checkin update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    private String shiftId;
    private String employeeFullName;
    private String subEmployeeId;
    private String subEmployeeFullName;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime checkinTime;

    public static SubCheckinResponseDTO convert(SubCheckin subCheckin) {
        return SubCheckinResponseDTO.builder()
                .id(subCheckin.getId().toString())
                .createdAt(subCheckin.getCreatedAt())
                .updatedAt(subCheckin.getUpdatedAt())
                .shiftId(subCheckin.getShift() != null ? subCheckin.getShift().getId().toString() : null)
                .employeeFullName(subCheckin.getShift().getEmployee() != null ? subCheckin.getShift().getEmployee().getUser().getFullName() : null)
                .subEmployeeId(subCheckin.getEmployee() != null ? subCheckin.getEmployee().getId().toString() : null)
                .subEmployeeFullName(subCheckin.getEmployee() != null ? subCheckin.getEmployee().getUser().getFullName() : null)
                .checkinTime(subCheckin.getCheckinTime())
                .startTime(subCheckin.getShift().getShiftStartTime())
                .endTime(subCheckin.getShift().getShiftEndTime())
                .build();
    }

    public static List<SubCheckinResponseDTO> convert(List<SubCheckin> subCheckins) {
        if (subCheckins == null || subCheckins.isEmpty()) {
            return List.of();
        }
        return subCheckins.stream()
                .map(SubCheckinResponseDTO::convert)
                .toList();
    }
}
