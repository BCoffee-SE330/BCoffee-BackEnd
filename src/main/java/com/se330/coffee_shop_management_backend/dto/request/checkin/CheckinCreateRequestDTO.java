package com.se330.coffee_shop_management_backend.dto.request.checkin;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CheckinCreateRequestDTO {
    private UUID shiftId;
    private LocalDateTime checkinTime;
}