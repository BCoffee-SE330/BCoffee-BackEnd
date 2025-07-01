package com.se330.coffee_shop_management_backend.dto.response.branch;

import com.se330.coffee_shop_management_backend.dto.response.AbstractBaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@SuperBuilder
public class BranchIdWithRevenueResponseDTO {

    @Schema(
            name = "id",
            description = "UUID",
            type = "String",
            example = "91b2999d-d327-4dc8-9956-2fadc0dc8778"
    )
    private String id;

    private String branchName;

    @Schema(
            name = "createdAt",
            description = "Date time field of branch creation",
            type = "LocalDateTime",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime createdAt;

    @Schema(
            name = "updatedAt",
            type = "LocalDateTime",
            description = "Date time field of branch update",
            example = "2022-09-29T22:37:31"
    )
    private LocalDateTime updatedAt;

    @Schema(
            name = "branchRevenue",
            description = "Revenue of the branch",
            type = "BigDecimal",
            example = "1000.000"
    )
    private BigDecimal branchRevenue;

    @Schema(
            name = "revenueByMonth",
            description = "Revenue of the branch by month",
            type = "List<Map<Integer, BigDecimal>>",
            example = "[{\"1\": 100.00}, {\"2\": 200.00}, {\"3\": 300.00}]"
    )
    private List<Map<Integer, BigDecimal>> revenueByMonth;
}
