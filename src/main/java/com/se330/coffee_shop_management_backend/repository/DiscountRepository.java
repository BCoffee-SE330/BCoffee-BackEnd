package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.entity.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID>, JpaSpecificationExecutor<Discount> {

    @Override
    @EntityGraph(attributePaths = {"branch", "productVariants", "productVariants.product"})
    Page<Discount> findAll(Pageable pageable);

    @Query("SELECT d FROM Discount d WHERE d.discountEndDate > CURRENT_TIMESTAMP AND d.discountStartDate <= CURRENT_TIMESTAMP AND d.discountIsActive = true")
    List<Discount> findAllActiveAndNotExpired();

    @Override
    @EntityGraph(attributePaths = {"branch", "productVariants", "productVariants.product"})
    Optional<Discount> findById(UUID id);

    @EntityGraph(attributePaths = {"branch", "productVariants", "productVariants.product"})
    Page<Discount> findAllByBranch(Branch existingBranch, Pageable pageable);

    @EntityGraph(attributePaths = {"branch", "productVariants", "productVariants.product"})
    Page<Discount> findAllByProductVariants_Id(UUID productVariantId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"branch", "productVariants"})
    List<Discount> findAll();

    @EntityGraph(attributePaths = {"branch", "productVariants"})
    Page<Discount> findAllByIdIn(Collection<UUID> ids, Pageable pageable);
}