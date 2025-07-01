package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.SubCheckin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubCheckinRepository extends JpaRepository<SubCheckin, UUID>, JpaSpecificationExecutor<SubCheckin> {

    @EntityGraph(attributePaths = {"employee", "shift", "shift.employee", "shift.employee.user"})
    Page<SubCheckin> findAllByShift_Id(UUID shiftId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"employee", "shift", "shift.employee", "shift.employee.user"})
    Page<SubCheckin> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"employee", "shift"})
    List<SubCheckin> findAll();

    @Override
    @EntityGraph(attributePaths = {"employee", "shift", "shift.employee", "shift.employee.user"})
    SubCheckin save(SubCheckin subCheckin);

    @EntityGraph(attributePaths = {"employee", "shift", "shift.employee", "shift.employee.user"})
    @Query("SELECT sc FROM SubCheckin sc WHERE sc.employee.id = :employeeId AND EXTRACT(MONTH FROM sc.checkinTime) = :month AND EXTRACT(YEAR FROM sc.checkinTime) = :year")
    List<SubCheckin> findAllByEmployee_IdAndMonthAndYear(@Param("employeeId") UUID employeeId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(sc) FROM SubCheckin sc WHERE sc.shift.id = :shiftId AND EXTRACT(MONTH FROM sc.checkinTime) = :month AND EXTRACT(YEAR FROM sc.checkinTime) = :year")
    Integer countAllByShift_IdAndMonthAndYear(@Param("shiftId") UUID shiftId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(sc) FROM SubCheckin sc WHERE sc.shift.employee.id = :employeeId AND EXTRACT(MONTH FROM sc.checkinTime) = :month AND EXTRACT(YEAR FROM sc.checkinTime) = :year")
    Integer countAllByShift_Employee_IdAndMonthAndYear(@Param("employeeId") UUID employeeId, @Param("month") int month, @Param("year") int year);
}
