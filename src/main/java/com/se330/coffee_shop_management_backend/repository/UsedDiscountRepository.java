package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.UsedDiscount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsedDiscountRepository extends JpaRepository<UsedDiscount, UUID>, JpaSpecificationExecutor<UsedDiscount> {
    @Query("SELECT COALESCE(SUM(ud.timesUse), 0) FROM UsedDiscount ud " +
            "JOIN ud.orderDetail od " +
            "JOIN od.order o " +
            "JOIN o.user u " +
            "WHERE ud.discount.id = :discountId AND u.id = :userId")
    int sumTimesUsedByUserAndDiscount(@Param("discountId") UUID discountId,
                                      @Param("userId") UUID userId);

    @Override
    @EntityGraph(attributePaths = {"discount", "orderDetail"})
    Optional<UsedDiscount> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"discount", "orderDetail"})
    Page<UsedDiscount> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"discount", "orderDetail"})
    List<UsedDiscount> findAll();
}
