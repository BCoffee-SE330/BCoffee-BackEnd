package com.se330.coffee_shop_management_backend.service.ingredientservices;

import com.se330.coffee_shop_management_backend.dto.request.ingredient.IngredientCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.ingredient.IngredientUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IIngredientService {
    Ingredient findByIdIngredient(UUID id);
    Page<Ingredient> findAllIngredients(Pageable pageable);
    Ingredient createIngredient(IngredientCreateRequestDTO ingredientCreateRequestDTO);
    Ingredient updateIngredient(IngredientUpdateRequestDTO ingredientUpdateRequestDTO);
    void deleteIngredient(UUID id);
}
