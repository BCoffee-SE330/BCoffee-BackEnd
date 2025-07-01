package com.se330.coffee_shop_management_backend.dto.response.checkin;

import com.se330.coffee_shop_management_backend.entity.Checkin;
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
public class CheckinResponseDTO {

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
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime checkinTime;

    public static CheckinResponseDTO convert(Checkin checkin) {
        return CheckinResponseDTO.builder()
                .id(checkin.getId().toString())
                .createdAt(checkin.getCreatedAt())
                .updatedAt(checkin.getUpdatedAt())
                .shiftId(checkin.getShift() != null ? checkin.getShift().getId().toString() : null)
                .employeeFullName(checkin.getShift().getEmployee() != null ? checkin.getShift().getEmployee().getUser().getFullName() : null)
                .checkinTime(checkin.getCheckinTime())
                .startTime(checkin.getShift().getShiftStartTime())
                .endTime(checkin.getShift().getShiftEndTime())
                .build();
    }

    public static List<CheckinResponseDTO> convert(List<Checkin> checkins) {
        if (checkins == null || checkins.isEmpty()) {
            return List.of();
        }

        return checkins.stream()
                .map(CheckinResponseDTO::convert)
                .toList();
    }
}