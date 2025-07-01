package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID> {
    @Override
    @EntityGraph(attributePaths = {"manager", "manager.user"})
    Page<Branch> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"manager", "manager.user"})
    List<Branch> findAll();

    @Override
    @EntityGraph(attributePaths = {"manager", "manager.user"})
    Optional<Branch> findById(UUID id);

    // Case 1: Filter by branch and year
    @Query("SELECT COALESCE(SUM(o.orderTotalCost), 0) FROM Order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE b.id = :branchId " +
            "AND EXTRACT(YEAR FROM o.oCreatedAt) = :year")
    Optional<BigDecimal> calculateTotalOrderCostByBranchAndYear(
            @Param("branchId") UUID branchId,
            @Param("year") int year);

    // Case 2: Filter by branch, month and year
    @Query("SELECT COALESCE(SUM(o.orderTotalCost), 0) FROM Order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE b.id = :branchId " +
            "AND EXTRACT(MONTH FROM o.oCreatedAt) = :month " +
            "AND EXTRACT(YEAR FROM o.oCreatedAt) = :year")
    Optional<BigDecimal> calculateTotalOrderCostByBranchAndMonthAndYear(
            @Param("branchId") UUID branchId,
            @Param("month") int month,
            @Param("year") int year);

    // Case 3: Filter by branch, day, month and year
    @Query("SELECT COALESCE(SUM(o.orderTotalCost), 0) FROM Order o " +
            "JOIN o.employee e " +
            "JOIN e.branch b " +
            "WHERE b.id = :branchId " +
            "AND EXTRACT(DAY FROM o.oCreatedAt) = :day " +
            "AND EXTRACT(MONTH FROM o.oCreatedAt) = :month " +
            "AND EXTRACT(YEAR FROM o.oCreatedAt) = :year")
    Optional<BigDecimal> calculateTotalOrderCostByBranchAndDayAndMonthAndYear(
            @Param("branchId") UUID branchId,
            @Param("day") int day,
            @Param("month") int month,
            @Param("year") int year);
}