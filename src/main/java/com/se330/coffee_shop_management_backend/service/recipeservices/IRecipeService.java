package com.se330.coffee_shop_management_backend.service.recipeservices;

import com.se330.coffee_shop_management_backend.dto.request.recipe.RecipeCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.recipe.RecipeUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IRecipeService {
    Recipe findByIdRecipe(UUID id);
    Page<Recipe> findAllRecipes(Pageable pageable);
    Recipe createRecipe(RecipeCreateRequestDTO recipeCreateRequestDTO);
    Recipe updateRecipe(RecipeUpdateRequestDTO recipeUpdateRequestDTO);
    void deleteRecipe(UUID id);
}
