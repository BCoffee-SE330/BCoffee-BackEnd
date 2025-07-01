package com.se330.coffee_shop_management_backend.service.shiftservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.shift.ShiftCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.shift.ShiftUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Employee;
import com.se330.coffee_shop_management_backend.entity.Shift;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.EmployeeRepository;
import com.se330.coffee_shop_management_backend.repository.ShiftRepository;
import com.se330.coffee_shop_management_backend.service.UserService;
import com.se330.coffee_shop_management_backend.service.checkinservices.ICheckinService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.service.shiftservices.IShiftService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
public class ImpShiftService implements IShiftService {

    private final ShiftRepository shiftRepository;
    private final ICheckinService checkinService;
    private final UserService userService;
    private final EmployeeRepository employeeRepository;
    private final INotificationService notificationService;

    public ImpShiftService(
            ShiftRepository shiftRepository,
            EmployeeRepository employeeRepository,
            UserService userService,
            ICheckinService checkinService,
            INotificationService notificationService
    ) {
        this.shiftRepository = shiftRepository;
        this.userService = userService;
        this.checkinService = checkinService;
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Shift findByIdShift(UUID id) {
        return shiftRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Shift> findAllShifts(Pageable pageable) {
        return shiftRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Shift> findAllShiftsByBranch(Pageable pageable) {
        UUID branchId = userService.getUser().getEmployee().getBranch().getId();
        return shiftRepository.findByEmployee_Branch_Id(branchId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Shift> findAllShiftsByEmployee(UUID employeeId, Pageable pageable) {
        return shiftRepository.findAllByEmployee_Id(employeeId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Shift> findAllShiftsByBranchAndDayOfWeekAndMonthAndYear(Constants.DayOfWeekEnum dayOfWeek, int month, int year, Pageable pageable) {
        UUID branchId = userService.getUser().getEmployee().getBranch().getId();
        return shiftRepository.findByEmployee_Branch_IdAndDayOfWeekAndMonthAndYear(branchId, dayOfWeek, month, year, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Map<Shift, Boolean>> findAllShiftsBySpecificDateOfBranch(int year, int month, int day, Pageable pageable) {
        UUID branchId = userService.getUser().getEmployee().getBranch().getId();
        LocalDate localDate = LocalDate.of(year, month, day);
        Constants.DayOfWeekEnum dayOfWeek = Constants.DayOfWeekEnum.valueOf(localDate.getDayOfWeek().name());

        Page<Shift> shiftPage = shiftRepository.findAllByEmployee_Branch_IdAndDayOfWeekAndMonthAndYear(
                branchId,
                dayOfWeek,
                month,
                year,
                pageable
        );

        return shiftPage.map(shift -> {
            boolean hasCheckin = checkinService.isCheckin(
                    shift.getId(),
                    day,
                    month,
                    year
            );
            return Map.of(shift, hasCheckin);
        });
    }

    @Override
    @Transactional
    public Shift createShift(ShiftCreateRequestDTO shiftCreateRequestDTO) {
        Employee employee = employeeRepository.findById(shiftCreateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        User employeeUser = employee.getUser();
        User manager = employee.getBranch().getManager().getUser();

        Shift newShift = shiftRepository.save(
                Shift.builder()
                        .employee(employee)
                        .shiftEndTime(shiftCreateRequestDTO.getShiftEndTime())
                        .shiftStartTime(shiftCreateRequestDTO.getShiftStartTime())
                        .dayOfWeek(shiftCreateRequestDTO.getDayOfWeek())
                        .month(shiftCreateRequestDTO.getMonth())
                        .year(shiftCreateRequestDTO.getYear())
                        .shiftSalary(shiftCreateRequestDTO.getShiftSalary())
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createNewShiftAssignmentContentManager(
                                employeeUser.getFullName(),
                                String.valueOf(newShift.getMonth()),
                                String.valueOf(newShift.getYear())
                        ))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createNewShiftAssignmentContent(
                                String.valueOf(newShift.getMonth()),
                                String.valueOf(newShift.getYear())
                        ))
                        .senderId(manager.getId())
                        .receiverId(employeeUser.getId())
                        .isRead(false)
                        .build()
        );

        return newShift;
    }

    @Transactional
    @Override
    public Shift updateShift(ShiftUpdateRequestDTO shiftUpdateRequestDTO) {
        Shift existingShift = shiftRepository.findById(shiftUpdateRequestDTO.getShiftId())
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        Employee employee = employeeRepository.findById(shiftUpdateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        User employeeUser = employee.getUser();
        User manager = employee.getBranch().getManager().getUser();

        if (existingShift.getEmployee() != null) {
            existingShift.getEmployee().getShifts().remove(existingShift);
            existingShift.setEmployee(employee);
            employee.getShifts().add(existingShift);
        }

        existingShift.setShiftStartTime(shiftUpdateRequestDTO.getShiftStartTime());
        existingShift.setShiftEndTime(shiftUpdateRequestDTO.getShiftEndTime());
        existingShift.setDayOfWeek(shiftUpdateRequestDTO.getDayOfWeek());
        existingShift.setMonth(shiftUpdateRequestDTO.getMonth());
        existingShift.setYear(shiftUpdateRequestDTO.getYear());
        existingShift.setShiftSalary(shiftUpdateRequestDTO.getShiftSalary());

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createShiftUpdatedContentForManager(employeeUser.getFullName()))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createShiftUpdatedContent(
                                String.valueOf(existingShift.getMonth()),
                                String.valueOf(existingShift.getYear())
                        ))
                        .senderId(manager.getId())
                        .receiverId(employeeUser.getId())
                        .isRead(false)
                        .build()
        );

        return shiftRepository.save(existingShift);
    }

    @Transactional
    @Override
    public void deleteShift(UUID id) {
        Shift existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        Employee employee = existingShift.getEmployee();
        User employeeUser = employee.getUser();
        User manager = employee.getBranch().getManager().getUser();

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createShiftDeletedContentForManager(employeeUser.getFullName()))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(CreateNotiContentHelper.createShiftDeletedContent(
                                String.valueOf(existingShift.getMonth()),
                                String.valueOf(existingShift.getYear())
                        ))
                        .senderId(manager.getId())
                        .receiverId(employeeUser.getId())
                        .isRead(false)
                        .build()
        );

        existingShift.getEmployee().getShifts().remove(existingShift);
        existingShift.setEmployee(null);

        shiftRepository.deleteById(id);
    }
}
