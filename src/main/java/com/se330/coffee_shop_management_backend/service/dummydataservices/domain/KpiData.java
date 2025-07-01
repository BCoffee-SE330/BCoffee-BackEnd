package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.CheckinRepository;
import com.se330.coffee_shop_management_backend.repository.SalaryRepository;
import com.se330.coffee_shop_management_backend.repository.ShiftRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class KpiData {

    private final UserRepository userRepository;
    private final SalaryRepository salaryRepository;
    private final CheckinRepository checkinRepository;
    private final ShiftRepository shiftRepository;

    @Transactional
    public void create() {
        createShifts();
        createCheckins();
        createSalaries();
    }

    private void createShifts() {
        log.info("Creating shifts...");

        // Get all employees and managers
        List<User> employeeUsers = userRepository.findAllByRoleName(Constants.RoleEnum.EMPLOYEE);
        List<User> managerUsers = userRepository.findAllByRoleName(Constants.RoleEnum.MANAGER);

        List<User> allStaff = new ArrayList<>();
        allStaff.addAll(employeeUsers);
        allStaff.addAll(managerUsers);

        if (allStaff.isEmpty()) {
            log.warn("No employees or managers found, skipping shifts creation");
            return;
        }

        Random random = new Random();
        List<Shift> shifts = new ArrayList<>();

        // Define shift times
        LocalTime morningStart = LocalTime.of(6, 0);
        LocalTime morningEnd = LocalTime.of(14, 0);
        LocalTime afternoonStart = LocalTime.of(14, 0);
        LocalTime afternoonEnd = LocalTime.of(22, 0);
        LocalTime nightStart = LocalTime.of(22, 0);
        LocalTime nightEnd = LocalTime.MIDNIGHT;

        // Current year and month
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        // Create shifts for current month and next month
        for (int monthOffset = 0; monthOffset < 2; monthOffset++) {
            int month = currentMonth + monthOffset;
            int year = currentYear;
            if (month > 12) {
                month -= 12;
                year += 1;
            }

            // Create all possible shifts for this month (3 shifts per day × 7 days)
            for (Constants.DayOfWeekEnum dayOfWeek : Constants.DayOfWeekEnum.values()) {
                // Morning shift
                createShiftWithEmployees(shifts, morningStart, morningEnd, dayOfWeek, month, year, allStaff, random);

                // Afternoon shift
                createShiftWithEmployees(shifts, afternoonStart, afternoonEnd, dayOfWeek, month, year, allStaff, random);

                // Night shift
                createShiftWithEmployees(shifts, nightStart, nightEnd, dayOfWeek, month, year, allStaff, random);
            }
        }

        shiftRepository.saveAll(shifts);
        log.info("Created {} shifts", shifts.size());
    }

    private void createShiftWithEmployees(List<Shift> shifts, LocalTime start, LocalTime end,
                                          Constants.DayOfWeekEnum dayOfWeek, int month, int year,
                                          List<User> staff, Random random) {
        // Assign about 30% of employees to each shift type
        List<User> shuffledStaff = new ArrayList<>(staff);
        Collections.shuffle(shuffledStaff);
        int staffCount = (int)(shuffledStaff.size() * 0.3);

        // Manager earns more than regular employees
        BigDecimal managerRate = BigDecimal.valueOf(150000);
        BigDecimal employeeRate = BigDecimal.valueOf(100000);

        for (int i = 0; i < staffCount; i++) {
            // Skip if we've used all staff
            if (i >= shuffledStaff.size()) break;

            User user = shuffledStaff.get(i);
            BigDecimal shiftSalary = user.getRole().getName().equals(Constants.RoleEnum.MANAGER) ?
                    managerRate : employeeRate;

            Shift shift = Shift.builder()
                    .shiftStartTime(start)
                    .shiftEndTime(end)
                    .employee(user.getEmployee())
                    .dayOfWeek(dayOfWeek)
                    .month(month)
                    .year(year)
                    .shiftSalary(shiftSalary)
                    .build();

            shifts.add(shift);
        }
    }

    private void createCheckins() {
        log.info("Creating checkins...");

        List<Shift> allShifts = shiftRepository.findAll();
        if (allShifts.isEmpty()) {
            log.warn("No shifts found, skipping checkin creation");
            return;
        }

        Random random = new Random();
        List<Checkin> checkins = new ArrayList<>();

        // Get current date information
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        int currentDayOfMonth = now.getDayOfMonth();

        // For each shift, create checkins for appropriate dates in the month
        for (Shift shift : allShifts) {
            // Only create checkins for past days in the current month or any day in previous months
            if (shift.getYear() < currentYear ||
                    (shift.getYear() == currentYear && shift.getMonth() < currentMonth) ||
                    (shift.getYear() == currentYear && shift.getMonth() == currentMonth)) {

                // Calculate all dates in the month that match this day of week
                List<LocalDateTime> matchingDates = getDatesForDayOfWeek(
                        shift.getYear(), shift.getMonth(), shift.getDayOfWeek());

                // Only include dates up to today for current month
                List<LocalDateTime> validDates = matchingDates.stream()
                        .filter(date -> !(shift.getMonth() == currentMonth && shift.getYear() == currentYear &&
                                date.getDayOfMonth() > currentDayOfMonth))
                        .toList();

                for (LocalDateTime date : validDates) {
                    // 80% chance of having a checkin (some absences)
                    if (random.nextDouble() <= 0.8) {
                        // Create 1-3 checkins per shift
                        int numberOfCheckins = random.nextInt(3) + 1;

                        for (int i = 0; i < numberOfCheckins; i++) {
                            // Generate checkin time based on shift start time
                            LocalDateTime checkinTime = date
                                    .withHour(shift.getShiftStartTime().getHour())
                                    .withMinute(shift.getShiftStartTime().getMinute())
                                    .plusMinutes(random.nextInt(30)); // Random minutes after start time

                            Checkin checkin = Checkin.builder()
                                    .shift(shift)
                                    .checkinTime(checkinTime)
                                    .build();

                            checkins.add(checkin);
                        }
                    }
                }
            }
        }

        checkinRepository.saveAll(checkins);
        log.info("Created {} checkins", checkins.size());
    }

    private List<LocalDateTime> getDatesForDayOfWeek(int year, int month, Constants.DayOfWeekEnum dayOfWeek) {
        // Convert our enum to java.time.DayOfWeek
        java.time.DayOfWeek javaDay;
        switch (dayOfWeek) {
            case MONDAY: javaDay = java.time.DayOfWeek.MONDAY; break;
            case TUESDAY: javaDay = java.time.DayOfWeek.TUESDAY; break;
            case WEDNESDAY: javaDay = java.time.DayOfWeek.WEDNESDAY; break;
            case THURSDAY: javaDay = java.time.DayOfWeek.THURSDAY; break;
            case FRIDAY: javaDay = java.time.DayOfWeek.FRIDAY; break;
            case SATURDAY: javaDay = java.time.DayOfWeek.SATURDAY; break;
            case SUNDAY: javaDay = java.time.DayOfWeek.SUNDAY; break;
            default: javaDay = java.time.DayOfWeek.MONDAY;
        }

        List<LocalDateTime> dates = new ArrayList<>();
        LocalDateTime date = LocalDateTime.of(year, month, 1, 0, 0);

        // Find first occurrence of the day
        while (date.getDayOfWeek() != javaDay) {
            date = date.plusDays(1);
        }

        // Add all occurrences of this day in the month
        while (date.getMonthValue() == month) {
            dates.add(date);
            date = date.plusDays(7);
        }

        return dates;
    }

    private void createSalaries() {
        log.info("Creating salaries...");

        // Get all shifts grouped by employee, month and year
        List<Shift> allShifts = shiftRepository.findAll();
        Map<Employee, Map<YearMonth, List<Shift>>> shiftsByEmployeeAndMonth = new HashMap<>();

        for (Shift shift : allShifts) {
            Employee employee = shift.getEmployee();
            YearMonth yearMonth = YearMonth.of(shift.getYear(), shift.getMonth());

            shiftsByEmployeeAndMonth
                    .computeIfAbsent(employee, k -> new HashMap<>())
                    .computeIfAbsent(yearMonth, k -> new ArrayList<>())
                    .add(shift);
        }

        // Calculate salaries based on shifts with checkins
        List<Salary> salaries = new ArrayList<>();

        for (Map.Entry<Employee, Map<YearMonth, List<Shift>>> employeeEntry : shiftsByEmployeeAndMonth.entrySet()) {
            Employee employee = employeeEntry.getKey();
            Map<YearMonth, List<Shift>> monthlyShifts = employeeEntry.getValue();

            for (Map.Entry<YearMonth, List<Shift>> monthEntry : monthlyShifts.entrySet()) {
                YearMonth yearMonth = monthEntry.getKey();
                List<Shift> shifts = monthEntry.getValue();

                BigDecimal monthSalary = BigDecimal.ZERO;

                for (Shift shift : shifts) {
                    // Only count shifts with checkins
                    if (!shift.getCheckins().isEmpty()) {
                        monthSalary = monthSalary.add(shift.getShiftSalary());
                    }
                }

                if (monthSalary.compareTo(BigDecimal.ZERO) > 0) {
                    Salary salary = Salary.builder()
                            .employee(employee)
                            .month(yearMonth.getMonthValue())
                            .year(yearMonth.getYear())
                            .monthSalary(monthSalary)
                            .build();

                    salaries.add(salary);
                }
            }
        }

        salaryRepository.saveAll(salaries);
        log.info("Created {} salary records", salaries.size());
    }
}
