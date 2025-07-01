package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.FavoriteDrink;
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

public interface FavoriteDrinkRepository extends JpaRepository<FavoriteDrink, UUID>, JpaSpecificationExecutor<FavoriteDrink> {
    @Query("SELECT fd.product.id FROM FavoriteDrink fd WHERE fd.user.id = :userId")
    Page<UUID> findProductIdsByUser_Id(@Param("userId") UUID userId, Pageable pageable);

    @Query(value = "SELECT fd.product.id FROM FavoriteDrink fd GROUP BY fd.product.id ORDER BY COUNT(fd) DESC",
            countQuery = "SELECT COUNT(DISTINCT fd.product.id) FROM FavoriteDrink fd")
    Page<UUID> findMostFavoritedProductIds(Pageable pageable);

    Optional<FavoriteDrink> findByUser_IdAndProduct_Id(UUID userId, UUID drinkId);

    boolean existsFavoriteDrinkByUser_IdAndProduct_Id(UUID userId, UUID productId);

    @Override
    @EntityGraph(attributePaths = {"user", "product"})
    List<FavoriteDrink> findAll();
}