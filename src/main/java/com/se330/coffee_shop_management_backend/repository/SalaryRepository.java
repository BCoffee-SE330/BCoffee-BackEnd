package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Salary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, UUID>, JpaSpecificationExecutor<Salary> {
    @Query("""
        SELECT COALESCE(SUM(s.shiftSalary), 0)
        FROM Shift s
        JOIN s.checkins c
        WHERE s.employee.id = :employeeId
        AND EXTRACT(MONTH FROM c.checkinTime) = :month
        AND EXTRACT(YEAR FROM c.checkinTime) = :year
    """)
    @EntityGraph(attributePaths = {"employee", "employee.shifts", "employee.user", "employee.user.role"})
    BigDecimal calculateTotalSalaryForEmployeeInMonthAndYear(
            @Param("employeeId") UUID employeeId,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("""
        SELECT COALESCE(SUM(s.shiftSalary), 0)
        FROM SubCheckin sc
        JOIN sc.shift s
        WHERE sc.employee.id = :employeeId
        AND EXTRACT(MONTH FROM sc.checkinTime) = :month
        AND EXTRACT(YEAR FROM sc.checkinTime) = :year
    """)
    @EntityGraph(attributePaths = {"employee", "employee.shifts", "employee.user", "employee.user.role"})
    BigDecimal calculateTotalSalaryForEmployeeInMonthAndYearForSubCheckins(
            @Param("employeeId") UUID employeeId,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("SELECT s FROM Salary s WHERE s.employee.id = :employeeId AND s.month = :month AND s.year = :year")
    @EntityGraph(attributePaths = {"employee", "employee.shifts", "employee.user", "employee.user.role"})
    Salary findByEmployeeIdAndMonthAndYear(
            @Param("employeeId") UUID employeeId,
            @Param("month") int month,
            @Param("year") int year
    );

    @Override
    @EntityGraph(attributePaths = {"employee", "employee.shifts", "employee.user", "employee.user.role"})
    Optional<Salary> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"employee", "employee.shifts", "employee.user", "employee.user.role"})
    Page<Salary> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"employee", "employee.shifts", "employee.user", "employee.user.role"})
    List<Salary> findAll();

    @Override
    @EntityGraph(attributePaths = {"employee", "employee.shifts", "employee.user.role"})
    Salary save(Salary salary);

    @EntityGraph(attributePaths = {"employee", "employee.shifts", "employee.user", "employee.user.role"})
    Page<Salary> findAllByEmployee_Branch_Id(UUID employeeBranchId, Pageable pageable);

    @EntityGraph(attributePaths = {"employee", "employee.shifts", "employee.user", "employee.user.role"})
    Page<Salary> findAllByEmployee_Branch_IdAndMonthAndYear(UUID branchId, int month, int year, Pageable pageable);
}