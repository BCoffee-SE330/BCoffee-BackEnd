package com.se330.coffee_shop_management_backend.service.employeeservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.auth.RegisterRequest;
import com.se330.coffee_shop_management_backend.dto.request.employee.EmployeeUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.user.CreateUserRequest;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.entity.Employee;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.EmployeeRepository;
import com.se330.coffee_shop_management_backend.service.AuthService;
import com.se330.coffee_shop_management_backend.service.RoleService;
import com.se330.coffee_shop_management_backend.service.UserService;
import com.se330.coffee_shop_management_backend.service.employeeservices.IEmployeeService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ImpEmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final UserService userService;
    private final RoleService roleService;
    private final INotificationService notificationService;

    public ImpEmployeeService(
            EmployeeRepository employeeRepository,
            BranchRepository branchRepository,
            UserService userService,
            AuthService authService,
            UserRepository userRepository,
            RoleService roleService,
            INotificationService notificationService
    ) {
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.authService = authService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findByIdEmployee(UUID id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> findAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> findAllEmployeesByBranchId(Pageable pageable) {
        User currentUser = userService.getUser();

        return employeeRepository.findAllByBranch_Id(currentUser.getEmployee().getBranch().getId(), pageable);
    }

    @Override
    @Transactional
    public Employee createEmployee(RegisterRequest request) throws BindException {
        Branch branch = userService.getUser().getEmployee().getBranch();

        User user = userService.register(request);

        if (user.getEmployee() != null) {
            throw new IllegalStateException("User with ID " + user.getId() + " is already assigned to an employee.");
        }

        user.getRole().getUsers().remove(user);
        user.setRole(roleService.findByName(Constants.RoleEnum.EMPLOYEE));
        userRepository.save(user);

        Employee newEmployee = employeeRepository.save(
                Employee.builder()
                        .employeeHireDate(LocalDateTime.now())
                        .branch(branch)
                        .user(user)
                        .build()
        );

        User manager = newEmployee.getBranch().getManager().getUser();
        User employeeUser = newEmployee.getUser();

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createWelcomeBranchContentManager(employeeUser.getFullName()))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createWelcomeBranchContent(branch.getBranchName()))
                        .senderId(manager.getId())
                        .receiverId(employeeUser.getId())
                        .isRead(false)
                        .build()
        );

        return findByIdEmployee(newEmployee.getId());
    }

    @Override
    @Transactional
    public Employee createBranchManager(RegisterRequest request, UUID branchId) throws BindException {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new EntityNotFoundException("Branch not found with ID: " + branchId));
        User user = userService.register(request);
        user.getRole().getUsers().remove(user);
        user.setRole(roleService.findByName(Constants.RoleEnum.MANAGER));
        userRepository.save(user);

        // find old manager acount and remove it from branch
        if (branch.getManager() != null) {
            User oldManager = branch.getManager().getUser();
            branch.setManager(null);
            branchRepository.save(branch);

            userService.delete(oldManager.getId().toString());
        }

        Employee manger = employeeRepository.save(
                Employee.builder()
                        .employeeHireDate(LocalDateTime.now())
                        .branch(branch)
                        .user(user)
                        .managedBranch(branch)
                        .build()
        );

        branch.setManager(manger);
        branchRepository.save(branch);

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createWelcomeBranchContent(branch.getBranchName()))
                        .senderId(null)
                        .receiverId(manger.getUser().getId())
                        .isRead(false)
                        .build()
        );

        return findByIdEmployee(manger.getId());
    }

    @Transactional
    @Override
    public Employee updateEmployee(EmployeeUpdateRequestDTO employeeUpdateRequestDTO) {
        Employee existingEmployee = employeeRepository.findById(employeeUpdateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + employeeUpdateRequestDTO.getEmployeeId()));

        existingEmployee.setEmployeeHireDate(employeeUpdateRequestDTO.getEmployeeHireDate());

        User manager = existingEmployee.getBranch().getManager().getUser();
        User employeeUser = existingEmployee.getUser();

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createEmployeeInfoUpdatedContentForManager(employeeUser.getFullName()))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createEmployeeInfoUpdatedContent())
                        .senderId(manager.getId())
                        .receiverId(employeeUser.getId())
                        .isRead(false)
                        .build()
        );

        employeeRepository.save(existingEmployee);

        return findByIdEmployee(existingEmployee.getId());
    }

    @Transactional
    @Override
    public void deleteEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));

        User user = employee.getUser();

        String userId = user != null ? user.getId().toString() : "null";

        if (employee.getBranch() != null) {
            employee.getBranch().getEmployees().remove(employee);
            employee.setBranch(null);
        }

        if (employee.getUser() != null) {
            employee.getUser().setEmployee(null);
            employee.setUser(null);
        }

        userService.delete(userId);
        employeeRepository.deleteById(id);
    }

}