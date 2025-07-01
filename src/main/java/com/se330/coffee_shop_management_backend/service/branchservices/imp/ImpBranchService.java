package com.se330.coffee_shop_management_backend.service.branchservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.branch.BranchCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.branch.BranchUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.branch.BranchIdWithRevenueResponseDTO;
import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.entity.Employee;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.EmployeeRepository;
import com.se330.coffee_shop_management_backend.service.RoleService;
import com.se330.coffee_shop_management_backend.service.branchservices.IBranchService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ImpBranchService implements IBranchService {

    private final BranchRepository branchRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleService roleService;
    private final INotificationService notificationService;

    public ImpBranchService(
            BranchRepository branchRepository,
            EmployeeRepository employeeRepository,
            RoleService roleService,
            INotificationService notificationService
    ) {
        this.branchRepository = branchRepository;
        this.employeeRepository = employeeRepository;
        this.roleService = roleService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Branch findByIdBranch(UUID id) {
        return branchRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Branch> findAllBranches(Pageable pageable) {
        return branchRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Branch createBranch(BranchCreateRequestDTO branchCreateRequestDTO) {
        Branch newBranch = branchRepository.save(
                Branch.builder()
                        .branchName(branchCreateRequestDTO.getBranchName())
                        .branchAddress(branchCreateRequestDTO.getBranchAddress())
                        .branchPhone(branchCreateRequestDTO.getBranchPhone())
                        .branchEmail(branchCreateRequestDTO.getBranchEmail())
                        .build()
        );

        notificationService.sendNotificationToAllUsers(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.BRANCH)
                        .notificationContent(CreateNotiContentHelper.createBranchAddedContent(
                                newBranch.getBranchName(),
                                newBranch.getBranchAddress()
                        ))
                        .senderId(null)
                        .receiverId(null)
                        .isRead(false)
                        .build()
        );

        return findByIdBranch(newBranch.getId());
    }

    @Override
    @Transactional
    public Branch updateBranch(BranchUpdateRequestDTO branchUpdateRequestDTO) {
        Branch existingBranch = branchRepository.findById(branchUpdateRequestDTO.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + branchUpdateRequestDTO.getBranchId()));

        if (branchUpdateRequestDTO.getManagerId() != null) {
            Employee branchManager = employeeRepository.findById(branchUpdateRequestDTO.getManagerId())
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + branchUpdateRequestDTO.getManagerId()));

            // Remove existing manager from the branch if it was previously set
            if (existingBranch.getManager() != null) {
                Employee currentManager = existingBranch.getManager();
                currentManager.setManagedBranch(null);

                // Remove MANAGER role from the current manager's user roles
                // set back to an employee role if the user is not a manager anymore
                currentManager.getUser().setRole(roleService.findByName(Constants.RoleEnum.EMPLOYEE));
            }

            existingBranch.setManager(branchManager);
            branchManager.setManagedBranch(existingBranch);

            // then add MANAGER role to user related to new employee
            branchManager.getUser().setRole(roleService.findByName(Constants.RoleEnum.MANAGER));
        }

        existingBranch.setBranchName(branchUpdateRequestDTO.getBranchName());
        existingBranch.setBranchAddress(branchUpdateRequestDTO.getBranchAddress());
        existingBranch.setBranchPhone(branchUpdateRequestDTO.getBranchPhone());
        existingBranch.setBranchEmail(branchUpdateRequestDTO.getBranchEmail());

        branchRepository.save(existingBranch);

        return findByIdBranch(existingBranch.getId());
    }

    @Override
    @Transactional
    public void deleteBranch(UUID id) {
        Branch existingBranch = branchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + id));

        // Remove existing manager from the branch if it was previously set
        if (existingBranch.getManager() != null) {
            Employee currentManager = existingBranch.getManager();
            currentManager.setManagedBranch(null);
        }

        // if the branch is removed, all the employees related to this branch will be removed as well

        branchRepository.deleteById(id);

        notificationService.sendNotificationToAllUsers(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.BRANCH)
                        .notificationContent(CreateNotiContentHelper.createBranchDeletedContent(
                                existingBranch.getBranchName(),
                                LocalDateTime.now().toString()
                        ))
                        .senderId(null)
                        .receiverId(null)
                        .isRead(false)
                        .build()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BranchIdWithRevenueResponseDTO getTotalOrderCostByBranchAndYear(UUID branchId, int year) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + branchId));

        BigDecimal totalRevenue = branchRepository.calculateTotalOrderCostByBranchAndYear(branchId, year)
                .orElse(BigDecimal.ZERO);
        BranchIdWithRevenueResponseDTO responseDTO = new BranchIdWithRevenueResponseDTO();
        responseDTO.setId(branch.getId().toString());
        responseDTO.setCreatedAt(branch.getCreatedAt());
        responseDTO.setUpdatedAt(branch.getUpdatedAt());
        responseDTO.setBranchName(branch.getBranchName());
        responseDTO.setBranchRevenue(totalRevenue);
        responseDTO.setRevenueByMonth(new ArrayList<>());
        for (Integer i = 1; i <= 12; i++) {
            BigDecimal monthlyRevenue = branchRepository.calculateTotalOrderCostByBranchAndMonthAndYear(branchId, i, year)
                    .orElse(BigDecimal.ZERO);
            responseDTO.getRevenueByMonth().add(Map.of(i, monthlyRevenue));
        }
        return responseDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalOrderCostByBranchAndMonthAndYear(UUID branchId, int month, int year) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + branchId));

        return branchRepository.calculateTotalOrderCostByBranchAndMonthAndYear(branchId, month, year)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalOrderCostByBranchAndDayAndMonthAndYear(UUID branchId, int day, int month, int year) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + branchId));

        return branchRepository.calculateTotalOrderCostByBranchAndDayAndMonthAndYear(branchId, day, month, year)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchIdWithRevenueResponseDTO> findAllBranchesWithRevenueWithYear(Pageable pageable, int year) {
        Page<Branch> branches = branchRepository.findAll(pageable);

        return branches.map(branch -> {
            BranchIdWithRevenueResponseDTO dto = new BranchIdWithRevenueResponseDTO();
            dto.setId(branch.getId().toString());
            dto.setCreatedAt(branch.getCreatedAt());
            dto.setUpdatedAt(branch.getUpdatedAt());
            dto.setBranchName(branch.getBranchName());

            // Calculate revenue for this branch
            BigDecimal revenue = branchRepository.calculateTotalOrderCostByBranchAndYear(branch.getId(), year)
                    .orElse(BigDecimal.ZERO);
            dto.setBranchRevenue(revenue);

            List<Map<Integer, BigDecimal>> revenueByMonth = new ArrayList<>();

            for (Integer i = 1; i <= 12; i++) {
                BigDecimal monthlyRevenue = branchRepository.calculateTotalOrderCostByBranchAndMonthAndYear(branch.getId(), i, year)
                        .orElse(BigDecimal.ZERO);
                revenueByMonth.add(Map.of(i, monthlyRevenue));
            }

            dto.setRevenueByMonth(revenueByMonth);

            return dto;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchIdWithRevenueResponseDTO> findAllBranchesWithRevenueWithMonthYear(Pageable pageable, int month, int year) {
        Page<Branch> branches = branchRepository.findAll(pageable);

        return branches.map(branch -> {
            BranchIdWithRevenueResponseDTO dto = new BranchIdWithRevenueResponseDTO();
            dto.setId(branch.getId().toString());
            dto.setCreatedAt(branch.getCreatedAt());
            dto.setUpdatedAt(branch.getUpdatedAt());
            dto.setBranchName(branch.getBranchName());


            // Calculate revenue for this branch
            BigDecimal revenue = branchRepository.calculateTotalOrderCostByBranchAndMonthAndYear(branch.getId(), month, year)
                    .orElse(BigDecimal.ZERO);
            dto.setBranchRevenue(revenue);

            return dto;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchIdWithRevenueResponseDTO> findAllBranchesWithRevenueWithDayMonthYear(Pageable pageable, int day, int month, int year) {
        Page<Branch> branches = branchRepository.findAll(pageable);

        return branches.map(branch -> {
            BranchIdWithRevenueResponseDTO dto = new BranchIdWithRevenueResponseDTO();
            dto.setId(branch.getId().toString());
            dto.setCreatedAt(branch.getCreatedAt());
            dto.setUpdatedAt(branch.getUpdatedAt());
            dto.setBranchName(branch.getBranchName());

            // Calculate revenue for this branch
            BigDecimal revenue = branchRepository.calculateTotalOrderCostByBranchAndDayAndMonthAndYear(branch.getId(), day, month, year)
                    .orElse(BigDecimal.ZERO);
            dto.setBranchRevenue(revenue);

            return dto;
        });
    }
}