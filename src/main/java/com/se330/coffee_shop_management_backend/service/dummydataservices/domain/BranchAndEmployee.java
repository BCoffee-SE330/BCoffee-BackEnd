package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.entity.Employee;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.EmployeeRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BranchAndEmployee {
    private final BranchRepository branchRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create() {
        createBranch();
        createEmployee();
    }

    private void createBranch() {
        log.info("Creating branches...");

        List<Branch> branches = new ArrayList<>();

        // Create 5 branches with Vietnamese names and locations
        branches.add(Branch.builder()
                .branchName("BCoffee Quận 1")
                .branchAddress("123 Nguyễn Huệ, Quận 1, TP.HCM")
                .branchPhone("028-3823-1234")
                .branchEmail("quan1@bcoffee.com")
                .build());

        branches.add(Branch.builder()
                .branchName("BCoffee Thủ Đức")
                .branchAddress("456 Võ Văn Ngân, Thủ Đức, TP.HCM")
                .branchPhone("028-3722-5678")
                .branchEmail("thuduc@bcoffee.com")
                .build());

        branches.add(Branch.builder()
                .branchName("BCoffee Quận 7")
                .branchAddress("789 Nguyễn Thị Thập, Quận 7, TP.HCM")
                .branchPhone("028-3775-9012")
                .branchEmail("quan7@bcoffee.com")
                .build());

        branches.add(Branch.builder()
                .branchName("BCoffee Tân Bình")
                .branchAddress("321 Cộng Hòa, Tân Bình, TP.HCM")
                .branchPhone("028-3811-3456")
                .branchEmail("tanbinh@bcoffee.com")
                .build());

        branches.add(Branch.builder()
                .branchName("BCoffee Bình Thạnh")
                .branchAddress("654 Xô Viết Nghệ Tĩnh, Bình Thạnh, TP.HCM")
                .branchPhone("028-3550-7890")
                .branchEmail("binhthanh@bcoffee.com")
                .build());

        branchRepository.saveAll(branches);
        log.info("Created {} branches", branches.size());
    }

    private void createEmployee() {
        log.info("Creating employees...");

        List<Branch> branches = branchRepository.findAll();
        if (branches.isEmpty()) {
            log.error("No branches found. Cannot create employees.");
            return;
        }

        List<User> availableUsers = userRepository.findAllByRoleName(Constants.RoleEnum.EMPLOYEE);
        List<User> availableManagers = userRepository.findAllByRoleName(Constants.RoleEnum.MANAGER);

        if (availableUsers.isEmpty()) {
            log.error("No available users found. Cannot create employees.");
            return;
        }

        List<Employee> employees = new ArrayList<>();
        List<User> referenceUsers = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Create 5 managers (1 for each branch)
        for (int i = 0; i < 5; i++) {
            Employee manager = Employee.builder()
                    .employeeHireDate(now.minusMonths((long) (i + 1) * 3))
                    .branch(branches.get(i))
                    .user(i < availableManagers.size() ? availableManagers.get(i) : null)
                    .build();
            employees.add(manager);
            User referenceUser = availableManagers.get(i); // I FUCKING HATE JPA
            referenceUser.setEmployee(manager);
            referenceUsers.add(referenceUser);
        }

        for (int i = 0; i < availableUsers.size(); i++) {
            int branchIndex = i % branches.size();

            User user = availableUsers.get(i);

            Employee employee = Employee.builder()
                    .employeeHireDate(now.minusMonths((long) (i % 12) + 1))
                    .branch(branches.get(branchIndex))
                    .user(user)
                    .build();

            employees.add(employee);
            user.setEmployee(employee);
            referenceUsers.add(user);
        }

        employeeRepository.saveAll(employees);
        userRepository.saveAll(referenceUsers);
        log.info("Created {} employees", employees.size());

        updateBranchManagers();
    }

    private void updateBranchManagers() {
        log.info("Updating branch managers...");

        List<Branch> branches = branchRepository.findAll();
        if (branches.isEmpty()) {
            log.error("No branches found. Cannot update managers.");
            return;
        }

        List<User> managerUsers = userRepository.findAllByRoleName(Constants.RoleEnum.MANAGER);

        List<Employee> managers = new ArrayList<>();
        for (User user : managerUsers) {
            Employee manager = user.getEmployee();
            if (manager != null && manager.getBranch() != null) {
                managers.add(manager);
            }
        }

        if (managers.size() < branches.size()) {
            log.error("Not enough managers found for all branches.");
            return;
        }

        // Assign each manager to their branch
        for (int i = 0; i < branches.size(); i++) {
            Branch branch = branches.get(i);
            Employee manager = managers.get(i);
            branch.setManager(manager);
        }

        branchRepository.saveAll(branches);
        log.info("Updated managers for {} branches", branches.size());
    }
}
