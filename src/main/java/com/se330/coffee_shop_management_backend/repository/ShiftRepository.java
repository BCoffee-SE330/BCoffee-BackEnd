package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Shift;
import com.se330.coffee_shop_management_backend.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID>, JpaSpecificationExecutor<Shift> {
    @Override
    @EntityGraph(attributePaths = {"employee", "employee.user"})
    Page<Shift> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"employee", "employee.user"})
    Page<Shift> findByEmployee_Branch_Id(UUID branchId, Pageable pageable);

    @EntityGraph(attributePaths = {"employee", "employee.user"})
    Page<Shift> findAllByEmployee_Id(UUID employeeId, Pageable pageable);

    @EntityGraph(attributePaths = {"employee", "employee.user"})
    Page<Shift> findByEmployee_Branch_IdAndDayOfWeekAndMonthAndYear(
            UUID branchId,
            Constants.DayOfWeekEnum dayOfWeek,
            int month,
            int year,
            Pageable pageable
    );

    @Override
    @EntityGraph(attributePaths = {"employee", "employee.user"})
    Shift save(Shift shift);

    @Override
    @EntityGraph(attributePaths = {"employee", "employee.user"})
    java.util.Optional<Shift> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"employee"})
    List<Shift> findAll();

    @EntityGraph(attributePaths = {"employee"})
    List<Shift> findAllByEmployee_IdAndMonthAndYear(UUID employeeId, int month, int year);

    @EntityGraph(attributePaths = {"employee", "employee.user"})
    Page<Shift> findAllByEmployee_Branch_IdAndDayOfWeekAndMonthAndYear(UUID employeeBranchId, Constants.DayOfWeekEnum dayOfWeek, int month, int year, Pageable pageable);
}
