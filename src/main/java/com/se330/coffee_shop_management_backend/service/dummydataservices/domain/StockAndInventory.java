package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.*;
import com.se330.coffee_shop_management_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockAndInventory {

    private final StockRepository stockRepository;
    private final InventoryRepository inventoryRepository;
    private final IngredientRepository ingredientRepository;
    private final WarehouseRepository warehouseRepository;
    private final BranchRepository branchRepository;

    @Transactional
    public void create() {
        createStock();
        createInventory();
    }

    private void createStock() {
        log.info("Creating stock records...");

        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<Warehouse> warehouses = warehouseRepository.findAll();

        if (ingredients.isEmpty() || warehouses.isEmpty()) {
            log.error("No ingredients or warehouses found. Cannot create stock records.");
            return;
        }

        List<Stock> stocks = new ArrayList<>();
        Random random = new Random();

        // Map ingredient types to appropriate units
        Map<String, String> unitsByType = new HashMap<>();
        unitsByType.put("COFFEE_BEANS", "grams");
        unitsByType.put("COFFEE_BASE", "ml");
        unitsByType.put("DAIRY", "ml");
        unitsByType.put("SYRUP", "ml");
        unitsByType.put("TEA_LEAVES", "grams");
        unitsByType.put("TEA_TOPPING", "grams");
        unitsByType.put("FRUIT", "grams");
        unitsByType.put("BASE", "grams");
        unitsByType.put("SWEETENER", "ml");
        unitsByType.put("TOPPING", "grams");
        unitsByType.put("POWDER", "grams");
        unitsByType.put("FLOUR", "grams");
        unitsByType.put("MEAT", "grams");
        unitsByType.put("CAKE_MIX", "grams");

        // Define warehouse specializations
        Map<String, List<String>> warehouseSpecializations = new HashMap<>();
        warehouseSpecializations.put("Kho Trung Tâm TP.HCM", Arrays.asList("COFFEE_BASE", "DAIRY", "SYRUP", "BASE", "SWEETENER", "TOPPING", "POWDER"));
        warehouseSpecializations.put("Kho Nguyên Liệu Đà Lạt", Arrays.asList("COFFEE_BEANS", "TEA_LEAVES", "FRUIT"));
        warehouseSpecializations.put("Kho Xuất Khẩu Miền Đông", Arrays.asList("COFFEE_BEANS", "TEA_LEAVES", "FLOUR", "CAKE_MIX"));
        warehouseSpecializations.put("Kho Vận Miền Tây", Arrays.asList("TEA_TOPPING", "FRUIT", "MEAT", "FLOUR"));

        // Create stock for each ingredient in all warehouses
        for (Ingredient ingredient : ingredients) {
            String ingredientType = ingredient.getIngredientType();
            String unit = unitsByType.getOrDefault(ingredientType, "grams");

            for (Warehouse warehouse : warehouses) {
                // Check if this warehouse specializes in this ingredient type
                boolean isSpecialized = warehouseSpecializations.getOrDefault(warehouse.getWarehouseName(), Collections.emptyList())
                        .contains(ingredientType);

                // Calculate base quantity based on ingredient type
                int baseQuantity = getBaseQuantity(ingredientType);

                // Adjust quantity based on warehouse specialization
                int quantity = isSpecialized ?
                        baseQuantity + random.nextInt(baseQuantity) : // More stock in specialized warehouses
                        Math.max(1, baseQuantity / 3 + random.nextInt(baseQuantity / 2)); // Less stock elsewhere

                stocks.add(Stock.builder()
                        .ingredient(ingredient)
                        .warehouse(warehouse)
                        .stockQuantity(quantity)
                        .stockUnit(unit)
                        .build());
            }
        }

        stockRepository.saveAll(stocks);
        log.info("Created {} stock records", stocks.size());
    }

    private int getBaseQuantity(String ingredientType) {
        // Return appropriate base quantities based on ingredient type
        switch (ingredientType) {
            case "COFFEE_BEANS":
                return 50000; // 50kg of coffee beans
            case "COFFEE_BASE":
                return 30000; // 30l of coffee base
            case "DAIRY":
                return 40000; // 40l of dairy products
            case "SYRUP":
                return 25000; // 25l of syrups
            case "TEA_LEAVES":
                return 20000; // 20kg of tea leaves
            case "TEA_TOPPING":
                return 15000; // 15kg of tea toppings
            case "FRUIT":
                return 25000; // 25kg of fruits
            case "BASE":
                return 100000; // 100kg of base ingredients (like ice)
            case "SWEETENER":
                return 35000; // 35l of sweeteners
            case "TOPPING":
                return 15000; // 15kg of toppings
            case "POWDER":
                return 10000; // 10kg of powders
            case "FLOUR":
                return 50000; // 50kg of flour
            case "MEAT":
                return 20000; // 20kg of meat products
            case "CAKE_MIX":
                return 25000; // 25kg of cake mixes
            default:
                return 15000; // Default quantity
        }
    }

    private void createInventory() {
        log.info("Creating inventory records...");

        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<Branch> branches = branchRepository.findAll();

        if (ingredients.isEmpty() || branches.isEmpty()) {
            log.error("No ingredients or branches found. Cannot create inventory records.");
            return;
        }

        List<Inventory> inventories = new ArrayList<>();
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();

        // Define branch sizes (affects inventory quantity)
        Map<String, Double> branchSizeMultipliers = new HashMap<>();
        branchSizeMultipliers.put("BCoffee Quận 1", 1.5);      // Largest branch
        branchSizeMultipliers.put("BCoffee Thủ Đức", 1.2);     // University area - high volume
        branchSizeMultipliers.put("BCoffee Quận 7", 1.3);      // Business district - high volume
        branchSizeMultipliers.put("BCoffee Tân Bình", 1.0);    // Standard branch
        branchSizeMultipliers.put("BCoffee Bình Thạnh", 0.8);  // Smaller branch

        // Create inventory for all combinations of branches and ingredients
        for (Branch branch : branches) {
            double sizeMultiplier = branchSizeMultipliers.getOrDefault(branch.getBranchName(), 1.0);

            for (Ingredient ingredient : ingredients) {
                String ingredientType = ingredient.getIngredientType();

                // Generate 5-10 inventory records per ingredient per branch
                int recordCount = 5 + random.nextInt(6); // Range from 5 to 10

                for (int i = 0; i < recordCount; i++) {
                    // Calculate base quantity for this inventory batch
                    int branchBaseQuantity = getBranchBaseQuantity(ingredientType);

                    // Adjust quantity based on branch size with some randomness
                    int quantity = (int) Math.round(branchBaseQuantity * sizeMultiplier *
                            (0.5 + random.nextDouble() * 0.8));

                    // Create diverse expiration dates based on probability distribution
                    int expirationCategory = getExpirationCategory(random);
                    LocalDateTime expireDate;

                    switch (expirationCategory) {
                        case 0: // Already expired (20%)
                            // Expired anywhere from yesterday to 60 days ago
                            expireDate = now.minusDays(1 + random.nextInt(60));
                            break;
                        case 1: // About to expire (25%)
                            // Will expire in the next 1-7 days
                            expireDate = now.plusDays(random.nextInt(7) + 1);
                            break;
                        case 2: // Mid-term expiration (40%)
                            // Will expire in 8-30 days
                            expireDate = now.plusDays(8 + random.nextInt(23));
                            break;
                        default: // Long-term expiration (15%)
                            // Will expire based on ingredient shelf life (50-100% of shelf life)
                            expireDate = now.plusDays((long) (ingredient.getShelfLifeDays() *
                                    (0.5 + random.nextDouble() * 0.5)));
                            break;
                    }

                    if (expireDate.isBefore(now)) {
                        log.debug("Created expired inventory record for {} at {}",
                                ingredient.getIngredientName(), branch.getBranchName());
                    }

                    // Create inventory record with varying quantities
                    // More expired items tend to have less quantity left
                    if (expireDate.isBefore(now)) {
                        // Expired items usually have less quantity left (10-40% of original)
                        quantity = (int)(quantity * (0.1 + random.nextDouble() * 0.3));
                    }

                    inventories.add(Inventory.builder()
                            .branch(branch)
                            .ingredient(ingredient)
                            .inventoryQuantity(quantity)
                            .inventoryExpireDate(expireDate)
                            .build());
                }
            }
        }

        inventoryRepository.saveAll(inventories);
        log.info("Created {} inventory records", inventories.size());
    }

    private int getExpirationCategory(Random random) {
        int value = random.nextInt(100);
        if (value < 20) return 0;      // 20% already expired
        else if (value < 45) return 1;  // 25% about to expire
        else if (value < 85) return 2;  // 40% mid-term expiration
        else return 3;                  // 15% long-term expiration
    }

    private int getBranchBaseQuantity(String ingredientType) {
        // Return appropriate base quantities for branches (smaller than warehouse quantities)
        switch (ingredientType) {
            case "COFFEE_BEANS":
                return 10000; // 10kg of coffee beans
            case "COFFEE_BASE":
                return 5000; // 5L of coffee base
            case "DAIRY":
                return 8000; // 8L of dairy products
            case "SYRUP":
                return 3000; // 3L of syrups
            case "TEA_LEAVES":
                return 4000; // 4kg of tea leaves
            case "TEA_TOPPING":
                return 3000; // 3kg of tea toppings
            case "FRUIT":
                return 5000; // 5kg of fruits
            case "BASE":
                return 15000; // 15kg of base ingredients (like ice)
            case "SWEETENER":
                return 6000; // 6L of sweeteners
            case "TOPPING":
                return 2000; // 2kg of toppings
            case "POWDER":
                return 2000; // 2kg of powders
            case "FLOUR":
                return 8000; // 8kg of flour
            case "MEAT":
                return 4000; // 4kg of meat products
            case "CAKE_MIX":
                return 5000; // 5kg of cake mixes
            default:
                return 3000; // Default quantity
        }
    }
}
