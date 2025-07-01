package com.se330.coffee_shop_management_backend.service.branchservices;

import com.se330.coffee_shop_management_backend.dto.request.branch.BranchCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.branch.BranchUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.branch.BranchIdWithRevenueResponseDTO;
import com.se330.coffee_shop_management_backend.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface IBranchService {
    Branch findByIdBranch(UUID id);
    Page<Branch> findAllBranches(Pageable pageable);
    Branch createBranch(BranchCreateRequestDTO branchCreateRequestDTO);
    Branch updateBranch(BranchUpdateRequestDTO branchUpdateRequestDTO);
    void deleteBranch(UUID id);
    BranchIdWithRevenueResponseDTO getTotalOrderCostByBranchAndYear (UUID branchId, int year);
    BigDecimal getTotalOrderCostByBranchAndMonthAndYear (UUID branchId, int month, int year);
    BigDecimal getTotalOrderCostByBranchAndDayAndMonthAndYear (UUID branchId, int day, int month, int year);
    Page<BranchIdWithRevenueResponseDTO> findAllBranchesWithRevenueWithYear(Pageable pageable, int year);
    Page<BranchIdWithRevenueResponseDTO> findAllBranchesWithRevenueWithMonthYear(Pageable pageable, int month, int year);
    Page<BranchIdWithRevenueResponseDTO> findAllBranchesWithRevenueWithDayMonthYear(Pageable pageable, int day, int month, int year);
}