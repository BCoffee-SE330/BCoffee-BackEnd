package com.se330.coffee_shop_management_backend.dto.request.branch;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class BranchUpdateRequestDTO {
    private UUID branchId;
    private String branchName;
    private String branchAddress;
    private String branchPhone;
    private String branchEmail;
    private UUID managerId;
}