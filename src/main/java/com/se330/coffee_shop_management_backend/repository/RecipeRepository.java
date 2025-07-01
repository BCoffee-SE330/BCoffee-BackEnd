package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID>, JpaSpecificationExecutor<Recipe> {
    @Override
    @EntityGraph(attributePaths = {"productVariant", "ingredient"})
    Recipe save(Recipe recipe);

    @Override
    @EntityGraph(attributePaths = {"productVariant", "ingredient"})
    Optional<Recipe> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"productVariant", "ingredient"})
    Page<Recipe> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"productVariant", "ingredient"})
    List<Recipe> findAll();
}