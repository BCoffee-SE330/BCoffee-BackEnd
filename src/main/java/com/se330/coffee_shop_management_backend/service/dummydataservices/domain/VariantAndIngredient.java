package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.Ingredient;
import com.se330.coffee_shop_management_backend.entity.Recipe;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.IngredientRepository;
import com.se330.coffee_shop_management_backend.repository.RecipeRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class VariantAndIngredient {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    @Transactional
    public void create() {
        createProductVariant();
        createIngredient();
        createRecipe();
    }

    private void createProductVariant() {
        log.info("Creating product variants...");

        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            log.error("Cannot create product variants: No products found");
            return;
        }

        List<ProductVariant> productVariants = new ArrayList<>();

        for (Product product : products) {
            String categoryName = product.getProductCategory().getCategoryName();

            // For beverages (Trà, Cà phê, Freeze), create S, M, L variants
            if (categoryName.equals("Trà") || categoryName.equals("Cà phê") || categoryName.equals("Freeze")) {
                // Size S variant (smallest)
                ProductVariant smallVariant = ProductVariant.builder()
                        .variantTierIdx("small")
                        .variantDefault(false)
                        .variantSlug(product.getProductSlug() + "-small")
                        .variantSort(1)
                        .variantPrice(BigDecimal.valueOf((product.getProductPrice().doubleValue() * 0.8)))
                        .variantIsPublished(true)
                        .variantIsDeleted(false)
                        .product(product)
                        .build();
                productVariants.add(smallVariant);

                // Size M variant (default)
                ProductVariant mediumVariant = ProductVariant.builder()
                        .variantTierIdx("medium")
                        .variantDefault(true)
                        .variantSlug(product.getProductSlug() + "-medium")
                        .variantSort(2)
                        .variantPrice(BigDecimal.valueOf(product.getProductPrice().longValue()))
                        .variantIsPublished(true)
                        .variantIsDeleted(false)
                        .product(product)
                        .build();
                productVariants.add(mediumVariant);

                // Size L variant (largest)
                ProductVariant largeVariant = ProductVariant.builder()
                        .variantTierIdx("large")
                        .variantDefault(false)
                        .variantSlug(product.getProductSlug() + "-large")
                        .variantSort(3)
                        .variantPrice(BigDecimal.valueOf((product.getProductPrice().doubleValue() * 1.2)))
                        .variantIsPublished(true)
                        .variantIsDeleted(false)
                        .product(product)
                        .build();
                productVariants.add(largeVariant);
            }
            // For non-beverages (Đồ ăn, Khác), create one default variant with the same properties as the product
            else {
                // Single default variant
                ProductVariant defaultVariant = ProductVariant.builder()
                        .variantTierIdx("default")
                        .variantDefault(true)
                        .variantSlug(product.getProductSlug() + "-default")
                        .variantSort(1)
                        .variantPrice(product.getProductPrice())
                        .variantIsPublished(product.getProductIsPublished())
                        .variantIsDeleted(product.getProductIsDeleted())
                        .product(product)
                        .build();
                productVariants.add(defaultVariant);
            }
        }

        productVariantRepository.saveAll(productVariants);
        log.info("Created {} product variants for {} products", productVariants.size(), products.size());
    }

    private void createIngredient() {
        log.info("Creating ingredients...");

        List<Ingredient> ingredients = new ArrayList<>();

        // Coffee ingredients
        ingredients.add(Ingredient.builder()
                .ingredientName("Hạt Cà Phê Arabica")
                .ingredientDescription("Hạt cà phê Arabica thượng hạng với hương vị mềm mại, hài hòa và độ acid nhẹ")
                .ingredientPrice(new BigDecimal("180000"))
                .ingredientType("COFFEE_BEANS")
                .shelfLifeDays(180) // 6 months
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Hạt Cà Phê Robusta")
                .ingredientDescription("Hạt cà phê Robusta đậm đà, mạnh mẽ với hàm lượng caffeine cao hơn, trồng tại Tây Nguyên")
                .ingredientPrice(new BigDecimal("150000"))
                .ingredientType("COFFEE_BEANS")
                .shelfLifeDays(180) // 6 months
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Shot Espresso")
                .ingredientDescription("Tinh chất cà phê đậm đặc, chiết xuất từ hỗn hợp hạt Arabica và Robusta thượng hạng")
                .ingredientPrice(new BigDecimal("12000"))
                .ingredientType("COFFEE_BASE")
                .shelfLifeDays(1) // 1 day
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Sữa Tươi")
                .ingredientDescription("Sữa tươi thanh trùng, nguyên chất với hương vị béo ngậy tự nhiên")
                .ingredientPrice(new BigDecimal("35000"))
                .ingredientType("DAIRY")
                .shelfLifeDays(7) // 7 days
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Sữa Đặc")
                .ingredientDescription("Sữa đặc ngọt, béo ngậy, lý tưởng để pha chế cà phê sữa kiểu Việt Nam")
                .ingredientPrice(new BigDecimal("40000"))
                .ingredientType("DAIRY")
                .shelfLifeDays(90) // 3 months
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Kem Tươi")
                .ingredientDescription("Kem tươi béo ngậy, dùng để trang trí và tạo lớp topping cho đồ uống")
                .ingredientPrice(new BigDecimal("55000"))
                .ingredientType("DAIRY")
                .shelfLifeDays(14) // 2 weeks
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Siro Socola")
                .ingredientDescription("Siro socola đậm đà, ngọt dịu, hoàn hảo để kết hợp với cà phê và kem")
                .ingredientPrice(new BigDecimal("75000"))
                .ingredientType("SYRUP")
                .shelfLifeDays(180) // 6 months
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Siro Caramel")
                .ingredientDescription("Siro caramel ngọt ngào với hương vị béo ngậy đặc trưng")
                .ingredientPrice(new BigDecimal("75000"))
                .ingredientType("SYRUP")
                .shelfLifeDays(180) // 6 months
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Siro Hạnh Nhân")
                .ingredientDescription("Siro hạnh nhân thơm béo, tạo hương vị đặc trưng cho các loại đồ uống")
                .ingredientPrice(new BigDecimal("85000"))
                .ingredientType("SYRUP")
                .shelfLifeDays(180) // 6 months
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Siro Vani")
                .ingredientDescription("Siro vani tinh khiết với hương thơm dịu nhẹ, ngọt ngào")
                .ingredientPrice(new BigDecimal("70000"))
                .ingredientType("SYRUP")
                .shelfLifeDays(180) // 6 months
                .build());

        // Tea ingredients
        ingredients.add(Ingredient.builder()
                .ingredientName("Lá Trà Xanh")
                .ingredientDescription("Lá trà xanh tươi, thu hái từ vùng cao nguyên, mang hương vị chát nhẹ và thanh mát")
                .ingredientPrice(new BigDecimal("120000"))
                .ingredientType("TEA_LEAVES")
                .shelfLifeDays(120) // 4 months
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Lá Trà Đen")
                .ingredientDescription("Lá trà đen được lên men hoàn toàn, cho hương vị đậm đà và màu sắc sâu")
                .ingredientPrice(new BigDecimal("130000"))
                .ingredientType("TEA_LEAVES")
                .shelfLifeDays(120) // 4 months
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Hạt Sen")
                .ingredientDescription("Hạt sen tươi, mềm, có vị ngọt nhẹ và hương thơm đặc trưng")
                .ingredientPrice(new BigDecimal("95000"))
                .ingredientType("TEA_TOPPING")
                .shelfLifeDays(14) // 2 weeks
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Củ Năng")
                .ingredientDescription("Củ năng tươi, giòn ngọt, thái lát mỏng dùng làm topping cho trà")
                .ingredientPrice(new BigDecimal("85000"))
                .ingredientType("TEA_TOPPING")
                .shelfLifeDays(14) // 2 weeks
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Đào Tươi")
                .ingredientDescription("Đào tươi ngon, ngọt mọng, thái lát mỏng để tạo hương vị và trang trí")
                .ingredientPrice(new BigDecimal("65000"))
                .ingredientType("FRUIT")
                .shelfLifeDays(7) // 1 week
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Đậu Đỏ")
                .ingredientDescription("Đậu đỏ hấp chín, dẻo thơm, ngọt tự nhiên, dùng làm topping cho trà")
                .ingredientPrice(new BigDecimal("45000"))
                .ingredientType("TEA_TOPPING")
                .shelfLifeDays(10) // 10 days
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Vải Tươi")
                .ingredientDescription("Vải tươi thơm ngọt, tạo hương vị đặc trưng cho trà vải")
                .ingredientPrice(new BigDecimal("85000"))
                .ingredientType("FRUIT")
                .shelfLifeDays(5) // 5 days
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Siro Đào")
                .ingredientDescription("Siro đào đậm đặc với hương vị ngọt thanh, dùng để pha chế trà đào")
                .ingredientPrice(new BigDecimal("75000"))
                .ingredientType("SYRUP")
                .shelfLifeDays(180) // 6 months
                .build());

        // Freeze & Common ingredients
        ingredients.add(Ingredient.builder()
                .ingredientName("Đá Viên")
                .ingredientDescription("Đá viên sạch, dùng để pha chế đồ uống lạnh và đồ uống đá xay")
                .ingredientPrice(new BigDecimal("5000"))
                .ingredientType("BASE")
                .shelfLifeDays(1) // 1 day
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Đường Nước")
                .ingredientDescription("Syrup đường tinh khiết, dùng để điều chỉnh độ ngọt cho đồ uống")
                .ingredientPrice(new BigDecimal("25000"))
                .ingredientType("SWEETENER")
                .shelfLifeDays(365) // 1 year
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Bánh Cookies Vụn")
                .ingredientDescription("Bánh cookies giòn tan nghiền vụn, dùng làm topping cho đồ uống đá xay")
                .ingredientPrice(new BigDecimal("55000"))
                .ingredientType("TOPPING")
                .shelfLifeDays(30) // 1 month
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Bột Socola")
                .ingredientDescription("Bột socola nguyên chất, đắng thanh, dùng để rắc lên đồ uống và làm nguyên liệu pha chế")
                .ingredientPrice(new BigDecimal("125000"))
                .ingredientType("POWDER")
                .shelfLifeDays(180) // 6 months
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Bột Trà Xanh")
                .ingredientDescription("Bột trà xanh Nhật Bản, hương vị đậm đà, dùng để làm freeze trà xanh")
                .ingredientPrice(new BigDecimal("185000"))
                .ingredientType("POWDER")
                .shelfLifeDays(180) // 6 months
                .build());

        // Food ingredients
        ingredients.add(Ingredient.builder()
                .ingredientName("Bột Mì Bánh Mì")
                .ingredientDescription("Bột mì protein cao, lý tưởng để làm bánh mì với vỏ giòn, ruột xốp")
                .ingredientPrice(new BigDecimal("65000"))
                .ingredientType("FLOUR")
                .shelfLifeDays(120) // 4 months
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Bơ Pháp")
                .ingredientDescription("Bơ nhập khẩu từ Pháp, béo ngậy, dùng làm bánh và các món ăn")
                .ingredientPrice(new BigDecimal("195000"))
                .ingredientType("DAIRY")
                .shelfLifeDays(60) // 2 months
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Pate Gan")
                .ingredientDescription("Pate gan thơm béo, được chế biến theo công thức đặc biệt của BCoffee")
                .ingredientPrice(new BigDecimal("85000"))
                .ingredientType("MEAT")
                .shelfLifeDays(14) // 2 weeks
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Kem Phô Mai")
                .ingredientDescription("Kem phô mai mềm mịn, thơm béo, dùng làm nhân bánh và topping")
                .ingredientPrice(new BigDecimal("105000"))
                .ingredientType("DAIRY")
                .shelfLifeDays(30) // 1 month
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Thịt Gà Xé")
                .ingredientDescription("Thịt gà tẩm ướp đặc biệt, xé sợi, dùng làm nhân bánh mì que")
                .ingredientPrice(new BigDecimal("120000"))
                .ingredientType("MEAT")
                .shelfLifeDays(5) // 5 days
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Thịt Bò Xé")
                .ingredientDescription("Thịt bò tẩm ướp đặc biệt, xé sợi, dùng làm nhân bánh mì que")
                .ingredientPrice(new BigDecimal("150000"))
                .ingredientType("MEAT")
                .shelfLifeDays(5) // 5 days
                .build());

        ingredients.add(Ingredient.builder()
                .ingredientName("Hỗn Hợp Bánh Tiramisu")
                .ingredientDescription("Hỗn hợp làm bánh tiramisu với hương vị đặc trưng của cà phê và cheese mascarpone")
                .ingredientPrice(new BigDecimal("165000"))
                .ingredientType("CAKE_MIX")
                .shelfLifeDays(7) // 1 week
                .build());

        ingredientRepository.saveAll(ingredients);
        log.info("Created {} ingredients", ingredients.size());
    }

    private void createRecipe() {
        log.info("Creating recipes...");

        List<Recipe> recipes = new ArrayList<>();
        List<ProductVariant> variants = productVariantRepository.findAll();
        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<Product> products = productRepository.findAll();

        // Map ingredients by type for easier access
        Map<String, List<Ingredient>> ingredientsByType = ingredients.stream()
                .collect(Collectors.groupingBy(Ingredient::getIngredientType));

        for (ProductVariant variant : variants) {
            Product product = variant.getProduct();
            String productName = product.getProductName().toUpperCase();
            String categoryName = product.getProductCategory().getCategoryName();

            // Coffee recipes
            if (categoryName.equals("Cà phê")) {
                createCoffeeRecipes(recipes, variant, ingredientsByType, productName);
            }
            // Tea recipes
            else if (categoryName.equals("Trà")) {
                createTeaRecipes(recipes, variant, ingredientsByType, productName);
            }
            // Freeze recipes
            else if (categoryName.equals("Freeze")) {
                createFreezeRecipes(recipes, variant, ingredientsByType, productName);
            }
            // Food recipes
            else if (categoryName.equals("Đồ ăn")) {
                createFoodRecipes(recipes, variant, ingredientsByType, productName);
            }
            // Other products recipes
            else if (categoryName.equals("Khác")) {
                createOtherRecipes(recipes, variant, ingredientsByType, productName);
            }
        }

        recipeRepository.saveAll(recipes);
        log.info("Created {} recipes", recipes.size());
    }

    private void createCoffeeRecipes(List<Recipe> recipes, ProductVariant variant,
                                     Map<String, List<Ingredient>> ingredientsByType, String productName) {
        // Base ingredients for all coffee drinks
        if (ingredientsByType.containsKey("COFFEE_BASE")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findRandomIngredient(ingredientsByType.get("COFFEE_BASE")))
                    .recipeQuantity(30)
                    .recipeUnit("ml")
                    .recipeIsTopping(false)
                    .build());
        }

        // Add coffee beans for all coffee products
        if (ingredientsByType.containsKey("COFFEE_BEANS")) {
            Ingredient coffeeBeans = findRandomIngredient(ingredientsByType.get("COFFEE_BEANS"));
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(coffeeBeans)
                    .recipeQuantity(18)
                    .recipeUnit("g")
                    .recipeIsTopping(false)
                    .build());
        }

        // Add milk/cream to milk-based coffee
        if (productName.contains("LATTE") || productName.contains("PHINDI") ||
                productName.contains("MACHIATO") || productName.contains("BẠC XĨU") || productName.contains("CAPUCHINO")) {
            if (ingredientsByType.containsKey("DAIRY")) {
                recipes.add(Recipe.builder()
                        .productVariant(variant)
                        .ingredient(findIngredientByNameContaining(ingredientsByType.get("DAIRY"), "Sữa Tươi"))
                        .recipeQuantity(120)
                        .recipeUnit("ml")
                        .recipeIsTopping(false)
                        .build());
            }
        }

        // Add condensed milk to Vietnamese coffee
        if (productName.contains("BẠC XĨU") || productName.contains("PHINDI KEM SỮA")) {
            if (ingredientsByType.containsKey("DAIRY")) {
                recipes.add(Recipe.builder()
                        .productVariant(variant)
                        .ingredient(findIngredientByNameContaining(ingredientsByType.get("DAIRY"), "Sữa Đặc"))
                        .recipeQuantity(30)
                        .recipeUnit("ml")
                        .recipeIsTopping(false)
                        .build());
            }
        }

        // Add cream topping for certain drinks
        if (productName.contains("MACHIATO") || productName.contains("CAPUCHINO")) {
            if (ingredientsByType.containsKey("DAIRY")) {
                recipes.add(Recipe.builder()
                        .productVariant(variant)
                        .ingredient(findIngredientByNameContaining(ingredientsByType.get("DAIRY"), "Kem Tươi"))
                        .recipeQuantity(20)
                        .recipeUnit("g")
                        .recipeIsTopping(true)
                        .build());
            }
        }

        // Add flavor syrups based on coffee type
        if (ingredientsByType.containsKey("SYRUP")) {
            String syrupType = null;
            if (productName.contains("CARAMEL")) syrupType = "Caramel";
            else if (productName.contains("MOCHA") || productName.contains("CHOCO")) syrupType = "Socola";
            else if (productName.contains("HẠNH NHÂN")) syrupType = "Hạnh Nhân";
            else if (productName.contains("VANI")) syrupType = "Vani";

            if (syrupType != null) {
                recipes.add(Recipe.builder()
                        .productVariant(variant)
                        .ingredient(findIngredientByNameContaining(ingredientsByType.get("SYRUP"), syrupType))
                        .recipeQuantity(15)
                        .recipeUnit("ml")
                        .recipeIsTopping(false)
                        .build());
            }
        }
    }

    private void createTeaRecipes(List<Recipe> recipes, ProductVariant variant,
                                  Map<String, List<Ingredient>> ingredientsByType, String productName) {
        // Base tea leaves for all tea drinks
        if (ingredientsByType.containsKey("TEA_LEAVES")) {
            String teaType = productName.contains("XANH") ? "Trà Xanh" : "Trà Đen";
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("TEA_LEAVES"), teaType))
                    .recipeQuantity(10)
                    .recipeUnit("g")
                    .recipeIsTopping(false)
                    .build());
        }

        // Add toppings based on tea type
        if (ingredientsByType.containsKey("TEA_TOPPING")) {
            if (productName.contains("SEN") && productName.contains("HẠT SEN")) {
                recipes.add(Recipe.builder()
                        .productVariant(variant)
                        .ingredient(findIngredientByNameContaining(ingredientsByType.get("TEA_TOPPING"), "Hạt Sen"))
                        .recipeQuantity(30)
                        .recipeUnit("g")
                        .recipeIsTopping(true)
                        .build());
            }
            else if (productName.contains("SEN") && productName.contains("CỦ NĂNG")) {
                recipes.add(Recipe.builder()
                        .productVariant(variant)
                        .ingredient(findIngredientByNameContaining(ingredientsByType.get("TEA_TOPPING"), "Củ Năng"))
                        .recipeQuantity(30)
                        .recipeUnit("g")
                        .recipeIsTopping(true)
                        .build());
            }
            else if (productName.contains("ĐẬU ĐỎ")) {
                recipes.add(Recipe.builder()
                        .productVariant(variant)
                        .ingredient(findIngredientByNameContaining(ingredientsByType.get("TEA_TOPPING"), "Đậu Đỏ"))
                        .recipeQuantity(40)
                        .recipeUnit("g")
                        .recipeIsTopping(true)
                        .build());
            }
        }

        // Add fruits based on tea type
        if (ingredientsByType.containsKey("FRUIT")) {
            if (productName.contains("ĐÀO")) {
                recipes.add(Recipe.builder()
                        .productVariant(variant)
                        .ingredient(findIngredientByNameContaining(ingredientsByType.get("FRUIT"), "Đào"))
                        .recipeQuantity(30)
                        .recipeUnit("g")
                        .recipeIsTopping(true)
                        .build());
            }
            else if (productName.contains("VẢI")) {
                recipes.add(Recipe.builder()
                        .productVariant(variant)
                        .ingredient(findIngredientByNameContaining(ingredientsByType.get("FRUIT"), "Vải"))
                        .recipeQuantity(30)
                        .recipeUnit("g")
                        .recipeIsTopping(true)
                        .build());
            }
        }

        // Add syrups for flavored tea
        if (ingredientsByType.containsKey("SYRUP")) {
            if (productName.contains("ĐÀO")) {
                recipes.add(Recipe.builder()
                        .productVariant(variant)
                        .ingredient(findIngredientByNameContaining(ingredientsByType.get("SYRUP"), "Đào"))
                        .recipeQuantity(20)
                        .recipeUnit("ml")
                        .recipeIsTopping(false)
                        .build());
            }
        }

        // Add ice for all tea drinks (they're typically served cold)
        if (ingredientsByType.containsKey("BASE")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("BASE"), "Đá"))
                    .recipeQuantity(120)
                    .recipeUnit("g")
                    .recipeIsTopping(false)
                    .build());
        }
    }

    private void createFreezeRecipes(List<Recipe> recipes, ProductVariant variant,
                                     Map<String, List<Ingredient>> ingredientsByType, String productName) {
        // Base ingredients for all freeze drinks
        if (ingredientsByType.containsKey("BASE")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("BASE"), "Đá"))
                    .recipeQuantity(200)
                    .recipeUnit("g")
                    .recipeIsTopping(false)
                    .build());
        }

        // Add milk to all freeze drinks
        if (ingredientsByType.containsKey("DAIRY")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("DAIRY"), "Sữa Tươi"))
                    .recipeQuantity(100)
                    .recipeUnit("ml")
                    .recipeIsTopping(false)
                    .build());
        }

        // Add cream topping for all freeze drinks
        if (ingredientsByType.containsKey("DAIRY")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("DAIRY"), "Kem Tươi"))
                    .recipeQuantity(30)
                    .recipeUnit("g")
                    .recipeIsTopping(true)
                    .build());
        }

        // Add flavor-specific ingredients
        if (productName.contains("TRÀ XANH") && ingredientsByType.containsKey("POWDER")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("POWDER"), "Trà Xanh"))
                    .recipeQuantity(15)
                    .recipeUnit("g")
                    .recipeIsTopping(false)
                    .build());
        }
        else if (productName.contains("CHOCO") && ingredientsByType.containsKey("POWDER")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("POWDER"), "Socola"))
                    .recipeQuantity(20)
                    .recipeUnit("g")
                    .recipeIsTopping(false)
                    .build());
        }
        else if (productName.contains("COOKIES") && ingredientsByType.containsKey("TOPPING")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("TOPPING"), "Cookies"))
                    .recipeQuantity(25)
                    .recipeUnit("g")
                    .recipeIsTopping(true)
                    .build());
        }

        // Add syrup for specific flavors
        if (ingredientsByType.containsKey("SYRUP")) {
            if (productName.contains("CARAMEL")) {
                recipes.add(Recipe.builder()
                        .productVariant(variant)
                        .ingredient(findIngredientByNameContaining(ingredientsByType.get("SYRUP"), "Caramel"))
                        .recipeQuantity(25)
                        .recipeUnit("ml")
                        .recipeIsTopping(false)
                        .build());
            }
        }

        // Add coffee for coffee-based freezes
        if (productName.contains("PHINDI") && ingredientsByType.containsKey("COFFEE_BASE")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("COFFEE_BASE"), "Espresso"))
                    .recipeQuantity(30)
                    .recipeUnit("ml")
                    .recipeIsTopping(false)
                    .build());
        }
    }

    private void createFoodRecipes(List<Recipe> recipes, ProductVariant variant,
                                   Map<String, List<Ingredient>> ingredientsByType, String productName) {
        // Bread base for all bakery items
        if ((productName.contains("BÁNH MÌ") || productName.contains("CROISSANT")) &&
                ingredientsByType.containsKey("FLOUR")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("FLOUR"), "Bột Mì"))
                    .recipeQuantity(80)
                    .recipeUnit("g")
                    .recipeIsTopping(false)
                    .build());
        }

        // Add butter for croissant and some bakery items
        if ((productName.contains("CROISSANT") || productName.contains("BÁNH NGỌT")) &&
                ingredientsByType.containsKey("DAIRY")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("DAIRY"), "Bơ"))
                    .recipeQuantity(25)
                    .recipeUnit("g")
                    .recipeIsTopping(false)
                    .build());
        }

        // Add specific fillings
        if (productName.contains("PATE") && ingredientsByType.containsKey("MEAT")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("MEAT"), "Pate"))
                    .recipeQuantity(30)
                    .recipeUnit("g")
                    .recipeIsTopping(false)
                    .build());
        }
        else if (productName.contains("GÀ") && ingredientsByType.containsKey("MEAT")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("MEAT"), "Gà"))
                    .recipeQuantity(40)
                    .recipeUnit("g")
                    .recipeIsTopping(false)
                    .build());
        }
        else if (productName.contains("BÒ") && ingredientsByType.containsKey("MEAT")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("MEAT"), "Bò"))
                    .recipeQuantity(40)
                    .recipeUnit("g")
                    .recipeIsTopping(false)
                    .build());
        }

        // Add cheese for items with cheese
        if (productName.contains("PHÔ MAI") && ingredientsByType.containsKey("DAIRY")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("DAIRY"), "Phô Mai"))
                    .recipeQuantity(20)
                    .recipeUnit("g")
                    .recipeIsTopping(productName.contains("BÁNH MÌ QUE"))
                    .build());
        }

        // Add tiramisu mix for tiramisu cake
        if (productName.contains("TIRAMISU") && ingredientsByType.containsKey("CAKE_MIX")) {
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("CAKE_MIX"), "Tiramisu"))
                    .recipeQuantity(100)
                    .recipeUnit("g")
                    .recipeIsTopping(false)
                    .build());
        }
    }

    private void createOtherRecipes(List<Recipe> recipes, ProductVariant variant,
                                    Map<String, List<Ingredient>> ingredientsByType, String productName) {
        // For packaged coffee products, use coffee beans
        if (productName.contains("GÓI CÀ PHÊ") && ingredientsByType.containsKey("COFFEE_BEANS")) {
            int quantity = productName.contains("1KG") ? 1000 : 200;
            recipes.add(Recipe.builder()
                    .productVariant(variant)
                    .ingredient(findIngredientByNameContaining(ingredientsByType.get("COFFEE_BEANS"), "Robusta"))
                    .recipeQuantity(quantity)
                    .recipeUnit("g")
                    .recipeIsTopping(false)
                    .build());
        }
        // For canned coffee, use coffee extract and additives
        else if (productName.contains("LON CÀ PHÊ")) {
            if (ingredientsByType.containsKey("COFFEE_BASE")) {
                recipes.add(Recipe.builder()
                        .productVariant(variant)
                        .ingredient(findIngredientByNameContaining(ingredientsByType.get("COFFEE_BASE"), "Espresso"))
                        .recipeQuantity(185 * 6) // 6 cans of 185ml
                        .recipeUnit("ml")
                        .recipeIsTopping(false)
                        .build());
            }

            // Add milk for milk coffee
            if (productName.contains("SỮA") && ingredientsByType.containsKey("DAIRY")) {
                recipes.add(Recipe.builder()
                        .productVariant(variant)
                        .ingredient(findIngredientByNameContaining(ingredientsByType.get("DAIRY"), "Sữa Đặc"))
                        .recipeQuantity(100)
                        .recipeUnit("ml")
                        .recipeIsTopping(false)
                        .build());
            }
        }
    }

    // Helper methods to find ingredients
    private Ingredient findRandomIngredient(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return null;
        }
        return ingredients.get(new Random().nextInt(ingredients.size()));
    }

    private Ingredient findIngredientByNameContaining(List<Ingredient> ingredients, String nameContains) {
        if (ingredients == null) {
            return null;
        }
        return ingredients.stream()
                .filter(i -> i.getIngredientName().contains(nameContains))
                .findFirst()
                .orElse(findRandomIngredient(ingredients));
    }
}
