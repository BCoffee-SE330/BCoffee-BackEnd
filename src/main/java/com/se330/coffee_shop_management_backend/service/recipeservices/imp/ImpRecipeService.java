package com.se330.coffee_shop_management_backend.service.recipeservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.recipe.RecipeCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.recipe.RecipeUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Ingredient;
import com.se330.coffee_shop_management_backend.entity.Recipe;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.IngredientRepository;
import com.se330.coffee_shop_management_backend.repository.RecipeRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.recipeservices.IRecipeService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpRecipeService implements IRecipeService {

    private final RecipeRepository recipeRepository;
    private final ProductVariantRepository productVariantRepository;
    private final IngredientRepository ingredientRepository;

    public ImpRecipeService(
            RecipeRepository recipeRepository,
            ProductVariantRepository productVariantRepository,
            IngredientRepository ingredientRepository
    ) {
        this.recipeRepository = recipeRepository;
        this.productVariantRepository = productVariantRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Recipe findByIdRecipe(UUID id) {
        return recipeRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recipe> findAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Recipe createRecipe(RecipeCreateRequestDTO recipeCreateRequestDTO) {

        ProductVariant existingProductVariant = productVariantRepository.findById(recipeCreateRequestDTO.getProductVariantId())
                .orElseThrow(() -> new RuntimeException("Product variant not found"));

        Ingredient existingIngredient = ingredientRepository.findById(recipeCreateRequestDTO.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        return recipeRepository.save(
                Recipe.builder()
                        .productVariant(existingProductVariant)
                        .ingredient(existingIngredient)
                        .recipeQuantity(recipeCreateRequestDTO.getRecipeQuantity())
                        .recipeUnit(recipeCreateRequestDTO.getRecipeUnit())
                        .recipeIsTopping(recipeCreateRequestDTO.isRecipeIsTopping())
                        .build()
        );
    }

    @Transactional
    @Override
    public Recipe updateRecipe(RecipeUpdateRequestDTO recipeUpdateRequestDTO) {
        Recipe existingRecipe = recipeRepository.findById(recipeUpdateRequestDTO.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        ProductVariant existingProductVariant = productVariantRepository.findById(recipeUpdateRequestDTO.getProductVariantId())
                .orElseThrow(() -> new RuntimeException("Product variant not found"));

        Ingredient existingIngredient = ingredientRepository.findById(recipeUpdateRequestDTO.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        if (existingRecipe.getProductVariant() != null) {
            existingRecipe.getProductVariant().getRecipes().remove(existingRecipe);
            existingRecipe.setProductVariant(existingProductVariant);
            existingProductVariant.getRecipes().add(existingRecipe);
        }

        if (existingRecipe.getIngredient() != null) {
            existingRecipe.getIngredient().getRecipes().remove(existingRecipe);
            existingRecipe.setIngredient(existingIngredient);
            existingIngredient.getRecipes().add(existingRecipe);
        }

        existingRecipe.setRecipeQuantity(recipeUpdateRequestDTO.getRecipeQuantity());
        existingRecipe.setRecipeUnit(recipeUpdateRequestDTO.getRecipeUnit());
        existingRecipe.setRecipeIsTopping(recipeUpdateRequestDTO.isRecipeIsTopping());

        return recipeRepository.save(existingRecipe);
    }

    @Transactional
    @Override
    public void deleteRecipe(UUID id) {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (existingRecipe.getProductVariant() != null) {
            existingRecipe.getProductVariant().getRecipes().remove(existingRecipe);
            existingRecipe.setProductVariant(null);
        }

        if (existingRecipe.getIngredient() != null) {
            existingRecipe.getIngredient().getRecipes().remove(existingRecipe);
            existingRecipe.setIngredient(null);
        }

        recipeRepository.deleteById(id);
    }
}
