package com.se330.coffee_shop_management_backend.dto.request.branch;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BranchCreateRequestDTO {
    private String branchName;
    private String branchAddress;
    private String branchPhone;
    private String branchEmail;
}