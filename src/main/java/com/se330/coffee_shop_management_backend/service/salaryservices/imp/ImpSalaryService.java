package com.se330.coffee_shop_management_backend.service.salaryservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.salary.SalaryCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.salary.SalaryUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.salary.SalaryDetailResponseDTO;
import com.se330.coffee_shop_management_backend.dto.response.salary.ShiftDetail;
import com.se330.coffee_shop_management_backend.dto.response.salary.SubShiftDetail;
import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.*;
import com.se330.coffee_shop_management_backend.service.UserService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.service.salaryservices.ISalaryService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImpSalaryService implements ISalaryService {

    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;
    private final INotificationService notificationService;
    private final UserService userService;
    private final CheckinRepository checkinRepository;
    private final ShiftRepository shiftRepository;
    private final SubCheckinRepository subCheckinRepository;

    public ImpSalaryService(
            SalaryRepository salaryRepository,
            EmployeeRepository employeeRepository,
            UserService userService,
            SubCheckinRepository subCheckinRepository,
            ShiftRepository shiftRepository,
            CheckinRepository checkinRepository,
            INotificationService notificationService
    ) {
        this.salaryRepository = salaryRepository;
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.subCheckinRepository = subCheckinRepository;
        this.shiftRepository = shiftRepository;
        this.notificationService = notificationService;
        this.checkinRepository = checkinRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Salary findById(UUID id) {
        return salaryRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Salary> findAll(Pageable pageable) {
        return salaryRepository.findAll(pageable);
    }

    @Override
    public Page<Salary> findAllByBranch(Pageable pageable) {
        UUID branchId = userService.getUser().getEmployee().getBranch().getId();
        return salaryRepository.findAllByEmployee_Branch_Id(branchId, pageable);
    }

    @Override
    public Page<Salary> findAllByBranchAndMonthAndYear(Pageable pageable, int month, int year) {
        UUID branchId = userService.getUser().getEmployee().getBranch().getId();
        return salaryRepository.findAllByEmployee_Branch_IdAndMonthAndYear(branchId, month, year, pageable);
    }

    @Override
    @Transactional
    public Salary create(SalaryCreateRequestDTO salaryCreateRequestDTO) {
        Employee employee = employeeRepository.findById(salaryCreateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + salaryCreateRequestDTO.getEmployeeId()));

        Salary salary = Salary.builder()
                .employee(employee)
                .month(salaryCreateRequestDTO.getMonth())
                .year(salaryCreateRequestDTO.getYear())
                .monthSalary(salaryCreateRequestDTO.getMonthSalary())
                .build();

        return salaryRepository.save(salary);
    }

    @Override
    @Transactional
    public Salary update(SalaryUpdateRequestDTO salaryUpdateRequestDTO) {
        Salary existingSalary = salaryRepository.findById(salaryUpdateRequestDTO.getSalaryId())
                .orElseThrow(() -> new EntityNotFoundException("Salary not found with id: " + salaryUpdateRequestDTO.getSalaryId()));

        Employee employee = employeeRepository.findById(salaryUpdateRequestDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + salaryUpdateRequestDTO.getEmployeeId()));

        // If an employee is changing, handle the relationships
        if (existingSalary.getEmployee() != null &&
                !existingSalary.getEmployee().getId().equals(salaryUpdateRequestDTO.getEmployeeId())) {
            existingSalary.getEmployee().getSalaries().remove(existingSalary);
        }

        existingSalary.setEmployee(employee);
        existingSalary.setMonth(salaryUpdateRequestDTO.getMonth());
        existingSalary.setYear(salaryUpdateRequestDTO.getYear());
        existingSalary.setMonthSalary(salaryUpdateRequestDTO.getMonthSalary());

        return salaryRepository.save(existingSalary);
    }

    /*
    * @Data
@NoArgsConstructor
@SuperBuilder
public class SalaryDetailResponseDTO {
    private String salaryId;
    private String employeeId;
    private String employeeName;
    private String monthAndYear;
    private String role;
    private int totalCheckins;
    private BigDecimal totalSalary;

    List<ShiftDetail> shiftDetails;
}

@Data
@NoArgsConstructor
@SuperBuilder
class ShiftDetail {
    private String shiftId;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal shiftSalary;
    private int totalShiftCheckins;
    private BigDecimal totalShiftSalary;
}
*
* public class SubShiftDetail {
    private String subShiftId;
    private String absentEmployeeId;
    private String absentEmployeeName;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal subShiftSalary;
    private int totalSubShiftCheckins;
    private BigDecimal totalSubShiftSalary;
}


    * */

    @Override
    @Transactional(readOnly = true)
    public SalaryDetailResponseDTO findSalaryDetailById(UUID id) {
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Salary not found with id: " + id));
        SalaryDetailResponseDTO salaryDetailResponseDTO = new SalaryDetailResponseDTO();
        salaryDetailResponseDTO.setSalaryId(salary.getId().toString());
        salaryDetailResponseDTO.setEmployeeId(salary.getEmployee().getId().toString());
        salaryDetailResponseDTO.setEmployeeName(salary.getEmployee().getUser().getFullName());
        salaryDetailResponseDTO.setMonthAndYear("Tháng " + salary.getMonth() + " Năm " + salary.getYear());
        salaryDetailResponseDTO.setRole(salary.getEmployee().getUser().getRole().getName().getValue());
        salaryDetailResponseDTO.setTotalCheckins(checkinRepository.countAllByShift_Employee_IdAndMonthAndYear(salary.getEmployee().getId(), salary.getMonth(), salary.getYear()));
        salaryDetailResponseDTO.setTotalSalary(salary.getMonthSalary());
        salaryDetailResponseDTO.setTotalSubCheckins(subCheckinRepository.countAllByShift_Employee_IdAndMonthAndYear(salary.getEmployee().getId(), salary.getMonth(), salary.getYear()));

        List<ShiftDetail> shiftDetails = new ArrayList<>();
        List<Shift> shifts = shiftRepository.findAllByEmployee_IdAndMonthAndYear(
                salary.getEmployee().getId(), salary.getMonth(), salary.getYear()
        );

        for (Shift shift : shifts) {
            ShiftDetail shiftDetail = new ShiftDetail();
            shiftDetail.setShiftId(shift.getId().toString());
            shiftDetail.setStartTime(shift.getShiftStartTime());
            shiftDetail.setEndTime(shift.getShiftEndTime());
            shiftDetail.setDaysOfWeek(shift.getDayOfWeek().toString());
            shiftDetail.setShiftSalary(shift.getShiftSalary());
            shiftDetail.setTotalShiftCheckins(checkinRepository.countAllByShift_IdAndMonthAndYear(shift.getId(), salary.getMonth(), salary.getYear()));
            shiftDetail.setTotalShiftSalary(shift.getShiftSalary().multiply(BigDecimal.valueOf(shiftDetail.getTotalShiftCheckins())));
            shiftDetails.add(shiftDetail);
        }

        List<SubShiftDetail> subShiftDetails = new ArrayList<>();
        List<SubCheckin> subCheckins = subCheckinRepository.findAllByEmployee_IdAndMonthAndYear(
                salary.getEmployee().getId(), salary.getMonth(), salary.getYear()
        );

        for (SubCheckin subCheckin : subCheckins) {
            SubShiftDetail subShiftDetail = new SubShiftDetail();
            subShiftDetail.setSubShiftId(subCheckin.getId().toString());
            subShiftDetail.setAbsentEmployeeId(subCheckin.getShift().getEmployee().getId().toString());
            subShiftDetail.setAbsentEmployeeName(subCheckin.getShift().getEmployee().getUser().getFullName());
            subShiftDetail.setDaysOfWeek(subCheckin.getShift().getDayOfWeek().toString());
            subShiftDetail.setStartTime(subCheckin.getShift().getShiftStartTime());
            subShiftDetail.setEndTime(subCheckin.getShift().getShiftEndTime());
            subShiftDetail.setSubShiftSalary(subCheckin.getShift().getShiftSalary());
            subShiftDetail.setTotalSubShiftCheckins(subCheckinRepository.countAllByShift_IdAndMonthAndYear(
                    subCheckin.getShift().getId(), salary.getMonth(), salary.getYear()
            ));
            subShiftDetail.setTotalSubShiftSalary(subCheckin.getShift().getShiftSalary());
            subShiftDetails.add(subShiftDetail);
        }

        salaryDetailResponseDTO.setShiftDetails(shiftDetails);
        salaryDetailResponseDTO.setSubShiftDetails(subShiftDetails);

        return salaryDetailResponseDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public SalaryDetailResponseDTO findMySalaryDetailByMonthAndYear(int month, int year) {
        Employee currentEmployee = userService.getUser().getEmployee();
        Salary salary = salaryRepository.findByEmployeeIdAndMonthAndYear(
                currentEmployee.getId(), month, year
        );

        return findSalaryDetailById(salary != null ? salary.getId() : null);
    }

    @Override
    @Transactional
    public void updateSalaryForAllEmployeesInBranchInMonthAndYear(int month, int year) {
        Branch existingBranch = userService.getUser().getEmployee().getBranch();

        User manager = userService.getUser();

        for (Employee employee : existingBranch.getEmployees()) {
            User employeeUser = employee.getUser();
            BigDecimal totalSalary = salaryRepository.calculateTotalSalaryForEmployeeInMonthAndYear(
                    employee.getId(), month, year
            );

            totalSalary = totalSalary.add(salaryRepository.calculateTotalSalaryForEmployeeInMonthAndYearForSubCheckins(
                    employee.getId(), month, year
            ));

            // if the salary for the month and year and employee already exists, update it
            Salary existingSalary = salaryRepository.findByEmployeeIdAndMonthAndYear(employee.getId(), month, year);

            if (existingSalary != null) {
                existingSalary.setMonthSalary(totalSalary);
                salaryRepository.save(existingSalary);

                notificationService.createNotification(
                        NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                                .notificationContent(
                                        CreateNotiContentHelper.createSalaryUpdatedContentForEmployee(
                                                String.valueOf(month),
                                                String.valueOf(year),
                                                String.valueOf(totalSalary))
                                )
                                .senderId(manager.getId())
                                .receiverId(employeeUser.getId())
                                .isRead(false)
                                .build()
                );
            } else {
                salaryRepository.save(
                        Salary.builder()
                                .employee(employee)
                                .month(month)
                                .year(year)
                                .monthSalary(totalSalary)
                                .build()
                );

                notificationService.createNotification(
                        NotificationCreateRequestDTO.builder()
                                .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                                .notificationContent(
                                        CreateNotiContentHelper.createSalaryCreatedContentForEmployee(
                                                String.valueOf(month),
                                                String.valueOf(year),
                                                String.valueOf(totalSalary))
                                )
                                .senderId(manager.getId())
                                .receiverId(employeeUser.getId())
                                .isRead(false)
                                .build()
                );
            }

            notificationService.createNotification(
                    NotificationCreateRequestDTO.builder()
                            .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                            .notificationContent(
                                    CreateNotiContentHelper.createSalaryCreatedContentForManagerAll(
                                            String.valueOf(month),
                                            String.valueOf(year)
                                    ))
                            .senderId(null)
                            .receiverId(manager.getId())
                            .isRead(false)
                            .build()
            );
        }
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Salary not found with id: " + id));

        Employee employee = salary.getEmployee();
        User employeeUser = employee.getUser();
        User manager = employee.getBranch().getManager().getUser();

        // Remove from employee
        if (employee != null) {
            employee.getSalaries().remove(salary);
            salary.setEmployee(null);
        }

        salaryRepository.delete(salary);

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(
                                CreateNotiContentHelper.createSalaryDeletedContentForEmployee(
                                        String.valueOf(salary.getMonth()),
                                        String.valueOf(salary.getYear())
                                ))
                        .senderId(manager.getId())
                        .receiverId(employeeUser.getId())
                        .isRead(false)
                        .build()
        );

        notificationService.createNotification(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.EMPLOYEE)
                        .notificationContent(
                                CreateNotiContentHelper.createSalaryDeletedContentForManager(
                                        employeeUser.getFullName(),
                                        String.valueOf(salary.getMonth()),
                                        String.valueOf(salary.getYear())
                                ))
                        .senderId(null)
                        .receiverId(manager.getId())
                        .isRead(false)
                        .build()
        );
    }
}