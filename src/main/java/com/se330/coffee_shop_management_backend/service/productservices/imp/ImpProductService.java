package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.notification.NotificationCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.NewProductCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductVariantCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.recipe.RecipeCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.product.BestSellingProductResponseDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.service.CloudinaryService;
import com.se330.coffee_shop_management_backend.service.notificationservices.INotificationService;
import com.se330.coffee_shop_management_backend.service.productservices.IProductService;
import com.se330.coffee_shop_management_backend.service.productservices.IProductVariantService;
import com.se330.coffee_shop_management_backend.service.recipeservices.IRecipeService;
import com.se330.coffee_shop_management_backend.util.Constants;
import com.se330.coffee_shop_management_backend.util.CreateNotiContentHelper;
import com.se330.coffee_shop_management_backend.util.CreateSlug;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.engine.search.query.SearchResult;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ImpProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final CloudinaryService cloudinaryService;
    private final INotificationService notificationService;
    private final IProductVariantService productVariantService;
    private final IRecipeService recipeService;

    @PersistenceContext
    private EntityManager entityManager;

    public ImpProductService(
            ProductRepository productRepository,
            ProductCategoryRepository productCategoryRepository,
            CloudinaryService cloudinaryService,
            INotificationService notificationService,
            IProductVariantService productVariantService,
            IRecipeService recipeService
    ) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.cloudinaryService = cloudinaryService;
        this.productVariantService = productVariantService;
        this.notificationService = notificationService;
        this.recipeService = recipeService;
    }

    @Transactional(readOnly = true)
    @Override
    public Product findByIdProduct(UUID id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Product> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> findAllProductsByCategory(UUID categoryId, Pageable pageable) {
        return productRepository.findAllByProductCategory_Id(categoryId, pageable);
    }

    @Override
    @Transactional
    public Product createProduct(ProductCreateRequestDTO productCreateRequestDTO) {
        ProductCategory category = productCategoryRepository.findById(productCreateRequestDTO.getProductCategory())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + productCreateRequestDTO.getProductCategory()));

        Product newProduct = productRepository.save(
                Product.builder()
                        .productCategory(category)
                        .productDescription(productCreateRequestDTO.getProductDescription())
                        .productPrice(productCreateRequestDTO.getProductPrice())
                        .productName(productCreateRequestDTO.getProductName())
                        .productThumb(cloudinaryService.getProductDefault())
                        .productIsDeleted(false)
                        .productIsPublished(true)
                        .productSlug(CreateSlug.createSlug(productCreateRequestDTO.getProductName()))
                        .productCommentCount(0)
                        .productRatingsAverage(BigDecimal.valueOf(0))
                        .build()
        );

        notificationService.sendNotificationToAllUsers(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.PRODUCT)
                        .notificationContent(CreateNotiContentHelper.createProductAddedContentAll(newProduct.getProductName()))
                        .senderId(null)
                        .receiverId(null)
                        .isRead(false)
                        .build()
        );

        return newProduct;
    }

    @Override
    public Product createProductWithImage(ProductCreateRequestDTO productCreateRequestDTO, MultipartFile file) throws Exception {
        ProductCategory category = productCategoryRepository.findById(productCreateRequestDTO.getProductCategory())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + productCreateRequestDTO.getProductCategory()));

        Product newProduct = productRepository.save(
                Product.builder()
                        .productCategory(category)
                        .productDescription(productCreateRequestDTO.getProductDescription())
                        .productPrice(productCreateRequestDTO.getProductPrice())
                        .productName(productCreateRequestDTO.getProductName())
                        .productThumb(cloudinaryService.getProductDefault())
                        .productIsDeleted(false)
                        .productIsPublished(true)
                        .productSlug(CreateSlug.createSlug(productCreateRequestDTO.getProductName()))
                        .productCommentCount(0)
                        .productRatingsAverage(BigDecimal.valueOf(0))
                        .build()
        );

        newProduct.setProductThumb(cloudinaryService.uploadFile(file, "product").get("url").toString());

        notificationService.sendNotificationToAllUsers(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.PRODUCT)
                        .notificationContent(CreateNotiContentHelper.createProductAddedContentAll(newProduct.getProductName()))
                        .senderId(null)
                        .receiverId(null)
                        .isRead(false)
                        .build()
        );

        return newProduct;
    }

    @Override
    @Transactional
    public Product createProductNew(NewProductCreateRequestDTO newProductCreateRequestDTO) {
        ProductCategory category = productCategoryRepository.findById(newProductCreateRequestDTO.getProductCategory())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + newProductCreateRequestDTO.getProductCategory()));

        // first create base product
        Product newProduct = productRepository.save(
                Product.builder()
                        .productCategory(category)
                        .productDescription(newProductCreateRequestDTO.getProductDescription())
                        .productPrice(newProductCreateRequestDTO.getProductPrice())
                        .productName(newProductCreateRequestDTO.getProductName())
                        .productThumb(cloudinaryService.getProductDefault())
                        .productIsDeleted(false)
                        .productIsPublished(true)
                        .productSlug(CreateSlug.createSlug(newProductCreateRequestDTO.getProductName()))
                        .productCommentCount(0)
                        .productRatingsAverage(BigDecimal.valueOf(0))
                        .build()
        );

        // check if the product is a drink
        if (newProductCreateRequestDTO.isDrink()) {
            // create 3 base variants for the drink
            ProductVariant smallVariant = productVariantService.createProductVariant(
                    ProductVariantCreateRequestDTO.builder()
                            .product(newProduct.getId())
                            .variantTierIdx(Constants.ProductVariantTierIdx.SMALL.getValue())
                            .variantDefault(true)
                            .variantPrice(newProduct.getProductPrice().multiply(BigDecimal.valueOf(0.8)))
                            .variantSort(1)
                            .build()
            );

            ProductVariant mediumVariant = productVariantService.createProductVariant(
                    ProductVariantCreateRequestDTO.builder()
                            .product(newProduct.getId())
                            .variantTierIdx(Constants.ProductVariantTierIdx.MEDIUM.getValue())
                            .variantDefault(false)
                            .variantPrice(newProduct.getProductPrice())
                            .variantSort(2)
                            .build()
            );

            ProductVariant largeVariant = productVariantService.createProductVariant(
                    ProductVariantCreateRequestDTO.builder()
                            .product(newProduct.getId())
                            .variantTierIdx(Constants.ProductVariantTierIdx.LARGE.getValue())
                            .variantDefault(false)
                            .variantPrice(newProduct.getProductPrice().multiply(BigDecimal.valueOf(1.2)))
                            .variantSort(3)
                            .build()
            );

            Map<UUID, Integer> productIngredients = newProductCreateRequestDTO.getProductIngredients();

            for (Map.Entry<UUID, Integer> entry : productIngredients.entrySet()) {
                UUID ingredientId = entry.getKey();
                int quantity = entry.getValue();

                // Create recipe for each variant
                recipeService.createRecipe(
                        RecipeCreateRequestDTO.builder()
                                .recipeQuantity((int) (quantity * 0.8))
                                .recipeUnit("grams")
                                .recipeIsTopping(false)
                                .ingredientId(ingredientId)
                                .productVariantId(smallVariant.getId())
                                .build()
                );

                recipeService.createRecipe(
                        RecipeCreateRequestDTO.builder()
                                .recipeQuantity(quantity)
                                .recipeUnit("grams")
                                .recipeIsTopping(false)
                                .ingredientId(ingredientId)
                                .productVariantId(mediumVariant.getId())
                                .build()
                );

                recipeService.createRecipe(
                        RecipeCreateRequestDTO.builder()
                                .recipeQuantity((int) (quantity * 1.2))
                                .recipeUnit("grams")
                                .recipeIsTopping(false)
                                .ingredientId(ingredientId)
                                .productVariantId(largeVariant.getId())
                                .build()
                );
            }

        } else {
            ProductVariant defaultVariant = productVariantService.createProductVariant(
                    ProductVariantCreateRequestDTO.builder()
                            .product(newProduct.getId())
                            .variantTierIdx(Constants.ProductVariantTierIdx.DEFAULT.getValue())
                            .variantDefault(true)
                            .variantPrice(newProduct.getProductPrice())
                            .variantSort(1)
                            .build()
            );

            // create recipe for the product
            Map<UUID, Integer> productIngredients = newProductCreateRequestDTO.getProductIngredients();
            for (Map.Entry<UUID, Integer> entry : productIngredients.entrySet()) {
                UUID ingredientId = entry.getKey();
                int quantity = entry.getValue();

                // Create recipe for the default variant
                recipeService.createRecipe(
                        RecipeCreateRequestDTO.builder()
                                .recipeQuantity(quantity)
                                .recipeUnit("grams")
                                .recipeIsTopping(false)
                                .ingredientId(ingredientId)
                                .productVariantId(defaultVariant.getId())
                                .build()
                );
            }
        }

        notificationService.sendNotificationToAllUsers(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.PRODUCT)
                        .notificationContent(CreateNotiContentHelper.createProductAddedContentAll(newProduct.getProductName()))
                        .senderId(null)
                        .receiverId(null)
                        .isRead(false)
                        .build()
        );

        return newProduct;
    }

    @Override
    @Transactional
    public Product createProductNewWithImage(NewProductCreateRequestDTO newProductCreateRequestDTO, MultipartFile file) throws Exception {
        ProductCategory category = productCategoryRepository.findById(newProductCreateRequestDTO.getProductCategory())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + newProductCreateRequestDTO.getProductCategory()));

        // First create base product with default image
        Product newProduct = productRepository.save(
                Product.builder()
                        .productCategory(category)
                        .productDescription(newProductCreateRequestDTO.getProductDescription())
                        .productPrice(newProductCreateRequestDTO.getProductPrice())
                        .productName(newProductCreateRequestDTO.getProductName())
                        .productThumb(cloudinaryService.getProductDefault()) // Default image initially
                        .productIsDeleted(false)
                        .productIsPublished(true)
                        .productSlug(CreateSlug.createSlug(newProductCreateRequestDTO.getProductName()))
                        .productCommentCount(0)
                        .productRatingsAverage(BigDecimal.valueOf(0))
                        .build()
        );

        // Upload and update with the actual image
        if (file != null && !file.isEmpty()) {
            Map uploadResult = cloudinaryService.uploadFile(file, "product");
            newProduct.setProductThumb(uploadResult.get("url").toString());
            productRepository.save(newProduct);
        }

        // Check if the product is a drink
        if (newProductCreateRequestDTO.isDrink()) {
            // Create 3 base variants for the drink
            ProductVariant smallVariant = productVariantService.createProductVariant(
                    ProductVariantCreateRequestDTO.builder()
                            .product(newProduct.getId())
                            .variantTierIdx(Constants.ProductVariantTierIdx.SMALL.getValue())
                            .variantDefault(true)
                            .variantPrice(newProduct.getProductPrice().multiply(BigDecimal.valueOf(0.8)))
                            .variantSort(1)
                            .build()
            );

            ProductVariant mediumVariant = productVariantService.createProductVariant(
                    ProductVariantCreateRequestDTO.builder()
                            .product(newProduct.getId())
                            .variantTierIdx(Constants.ProductVariantTierIdx.MEDIUM.getValue())
                            .variantDefault(false)
                            .variantPrice(newProduct.getProductPrice())
                            .variantSort(2)
                            .build()
            );

            ProductVariant largeVariant = productVariantService.createProductVariant(
                    ProductVariantCreateRequestDTO.builder()
                            .product(newProduct.getId())
                            .variantTierIdx(Constants.ProductVariantTierIdx.LARGE.getValue())
                            .variantDefault(false)
                            .variantPrice(newProduct.getProductPrice().multiply(BigDecimal.valueOf(1.2)))
                            .variantSort(3)
                            .build()
            );

            Map<UUID, Integer> productIngredients = newProductCreateRequestDTO.getProductIngredients();

            for (Map.Entry<UUID, Integer> entry : productIngredients.entrySet()) {
                UUID ingredientId = entry.getKey();
                int quantity = entry.getValue();

                // Create recipe for each variant
                recipeService.createRecipe(
                        RecipeCreateRequestDTO.builder()
                                .recipeQuantity((int) (quantity * 0.8))
                                .recipeUnit("grams")
                                .recipeIsTopping(false)
                                .ingredientId(ingredientId)
                                .productVariantId(smallVariant.getId())
                                .build()
                );

                recipeService.createRecipe(
                        RecipeCreateRequestDTO.builder()
                                .recipeQuantity(quantity)
                                .recipeUnit("grams")
                                .recipeIsTopping(false)
                                .ingredientId(ingredientId)
                                .productVariantId(mediumVariant.getId())
                                .build()
                );

                recipeService.createRecipe(
                        RecipeCreateRequestDTO.builder()
                                .recipeQuantity((int) (quantity * 1.2))
                                .recipeUnit("grams")
                                .recipeIsTopping(false)
                                .ingredientId(ingredientId)
                                .productVariantId(largeVariant.getId())
                                .build()
                );
            }
        } else {
            // For non-drink products, create a default variant
            ProductVariant defaultVariant = productVariantService.createProductVariant(
                    ProductVariantCreateRequestDTO.builder()
                            .product(newProduct.getId())
                            .variantTierIdx(Constants.ProductVariantTierIdx.DEFAULT.getValue())
                            .variantDefault(true)
                            .variantPrice(newProduct.getProductPrice())
                            .variantSort(1)
                            .build()
            );

            // Create recipe for the product
            Map<UUID, Integer> productIngredients = newProductCreateRequestDTO.getProductIngredients();
            for (Map.Entry<UUID, Integer> entry : productIngredients.entrySet()) {
                UUID ingredientId = entry.getKey();
                int quantity = entry.getValue();

                // Create recipe for the default variant
                recipeService.createRecipe(
                        RecipeCreateRequestDTO.builder()
                                .recipeQuantity(quantity)
                                .recipeUnit("grams")
                                .recipeIsTopping(false)
                                .ingredientId(ingredientId)
                                .productVariantId(defaultVariant.getId())
                                .build()
                );
            }
        }

        notificationService.sendNotificationToAllUsers(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.PRODUCT)
                        .notificationContent(CreateNotiContentHelper.createProductAddedContentAll(newProduct.getProductName()))
                        .senderId(null)
                        .receiverId(null)
                        .isRead(false)
                        .build()
        );

        return newProduct;
    }

    @Transactional
    @Override
    public Product updateProduct(ProductUpdateRequestDTO productUpdateRequestDTO) {
        Product existingProduct = productRepository.findById(productUpdateRequestDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productUpdateRequestDTO.getProductId()));

        ProductCategory category = productCategoryRepository.findById(productUpdateRequestDTO.getProductCategory())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + productUpdateRequestDTO.getProductCategory()));

        if (existingProduct.getProductCategory() != null) {
            existingProduct.getProductCategory().getProducts().remove(existingProduct);
            existingProduct.setProductCategory(category);
            category.getProducts().add(existingProduct);
        }

        existingProduct.setProductDescription(productUpdateRequestDTO.getProductDescription());
        existingProduct.setProductPrice(productUpdateRequestDTO.getProductPrice());
        existingProduct.setProductName(productUpdateRequestDTO.getProductName());
        existingProduct.setProductIsDeleted(productUpdateRequestDTO.getProductIsDeleted());
        existingProduct.setProductSlug(CreateSlug.createSlug(productUpdateRequestDTO.getProductName()));
        existingProduct.setProductIsPublished(productUpdateRequestDTO.getProductIsPublished());
        existingProduct.setProductRatingsAverage(productUpdateRequestDTO.getProductRatingsAverage());

        productRepository.save(existingProduct);

        notificationService.sendNotificationToAllUsers(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.PRODUCT)
                        .notificationContent(CreateNotiContentHelper.createProductUpdatedContentAll(existingProduct.getProductName()))
                        .senderId(null)
                        .receiverId(null)
                        .isRead(false)
                        .build()
        );

        return existingProduct;
    }

    @Transactional
    @Override
    public void deleteProduct(UUID id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

        if (existingProduct.getProductCategory() != null) {
            existingProduct.getProductCategory().getProducts().remove(existingProduct);
            existingProduct.setProductCategory(null);
        }


        productRepository.deleteById(id);

        notificationService.sendNotificationToAllUsers(
                NotificationCreateRequestDTO.builder()
                        .notificationType(Constants.NotificationTypeEnum.PRODUCT)
                        .notificationContent(CreateNotiContentHelper.createProductDeletedContentAll(existingProduct.getProductName()))
                        .senderId(null)
                        .receiverId(null)
                        .isRead(false)
                        .build()
        );

    }

    @Transactional(readOnly = true)
    @Override
    public Page<BestSellingProductResponseDTO> findAllBestSellingProducts(Pageable pageable) {
        List<Product> productData = productRepository.findAllBestSellingProductsList();
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(productData);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bestSellingProducts.size());
        List<BestSellingProductResponseDTO> pagedList = bestSellingProducts.subList(start, end);
        return new PageImpl<>(pagedList, pageable, bestSellingProducts.size());
    }


    @Transactional(readOnly = true)
    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByYear(int year, Pageable pageable) {
        List<Product> products = productRepository.findBestSellingProductsByYear(year);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(products);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bestSellingProducts.size());
        List<BestSellingProductResponseDTO> pagedList = bestSellingProducts.subList(start, end);
        return new PageImpl<>(pagedList, pageable, bestSellingProducts.size());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByMonthAndYear(int month, int year, Pageable pageable) {
        List<Product> products = productRepository.findBestSellingProductsByMonthAndYear(month, year);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(products);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bestSellingProducts.size());
        List<BestSellingProductResponseDTO> pagedList = bestSellingProducts.subList(start, end);
        return new PageImpl<>(pagedList, pageable, bestSellingProducts.size());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByDayAndMonthAndYear(int day, int month, int year, Pageable pageable) {
        List<Product> products = productRepository.findBestSellingProductsByDayAndMonthAndYear(day, month, year);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(products);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bestSellingProducts.size());
        List<BestSellingProductResponseDTO> pagedList = bestSellingProducts.subList(start, end);
        return new PageImpl<>(pagedList, pageable, bestSellingProducts.size());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByBranch(UUID branchId, Pageable pageable) {
        List<Product> products = productRepository.findBestSellingProductsByBranch(branchId);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(products);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bestSellingProducts.size());
        List<BestSellingProductResponseDTO> pagedList = bestSellingProducts.subList(start, end);
        return new PageImpl<>(pagedList, pageable, bestSellingProducts.size());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByBranchAndYear(UUID branchId, int year, Pageable pageable) {
        List<Product> products = productRepository.findBestSellingProductsByBranchAndYear(branchId, year);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(products);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bestSellingProducts.size());
        List<BestSellingProductResponseDTO> pagedList = bestSellingProducts.subList(start, end);
        return new PageImpl<>(pagedList, pageable, bestSellingProducts.size());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByBranchAndMonthAndYear(UUID branchId, int month, int year, Pageable pageable) {
        List<Product> products = productRepository.findBestSellingProductsByBranchAndMonthAndYear(branchId, month, year);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(products);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bestSellingProducts.size());
        List<BestSellingProductResponseDTO> pagedList = bestSellingProducts.subList(start, end);
        return new PageImpl<>(pagedList, pageable, bestSellingProducts.size());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BestSellingProductResponseDTO> findBestSellingProductsByBranchAndDayAndMonthAndYear(UUID branchId, int day, int month, int year, Pageable pageable) {
        List<Product> products = productRepository.findBestSellingProductsByBranchAndDayAndMonthAndYear(branchId, day, month, year);
        List<BestSellingProductResponseDTO> bestSellingProducts = BestSellingProductResponseDTO.convert(products);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bestSellingProducts.size());
        List<BestSellingProductResponseDTO> pagedList = bestSellingProducts.subList(start, end);
        return new PageImpl<>(pagedList, pageable, bestSellingProducts.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        // Return all products if keyword is empty
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll(pageable);
        }

        // Fix: Use correct method to create search session
        SearchSession searchSession = Search.session(entityManager);

        // Execute the search using the vietnamese_search analyzer
        SearchResult<Product> result = searchSession.search(Product.class)
                .where(f -> f.match()
                        .fields("productName")
                        .matching(keyword)
                        .analyzer("vietnamese_search"))
                .fetch((int) pageable.getOffset(), pageable.getPageSize());

        List<Product> hits = result.hits();
        long totalHitCount = result.total().hitCount();

        return new PageImpl<>(hits, pageable, totalHitCount);
    }

    @Transactional
    @Override
    public String uploadProductImage(UUID productId, MultipartFile file) throws Exception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        // Check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        // Check if the current file is the default image
        if (existingProduct.getProductThumb().equals(cloudinaryService.getProductDefault())) {
            // Upload the new image
            Map uploadResult = cloudinaryService.uploadFile(file, "product");
            String imageUrl = uploadResult.get("url").toString();
            // Update the product's image URL
            existingProduct.setProductThumb(imageUrl);
            productRepository.save(existingProduct);

            return imageUrl;
        } else {
            // Delete the old image from Cloudinary
            try {
                cloudinaryService.deleteFile(existingProduct.getProductThumb());
            } catch (IOException e) {
                log.error("Failed to delete old product image: {}", e.getMessage());
                // Continue with upload even if delete fails
            }

            // Upload the new image
            Map uploadResult = cloudinaryService.uploadFile(file, "product");
            String imageUrl = uploadResult.get("url").toString();

            // Update the product's image URL
            existingProduct.setProductThumb(imageUrl);
            productRepository.save(existingProduct);

            return imageUrl;
        }
    }

    @Transactional
    @Override
    public String deleteProductImage(UUID productId) throws Exception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        String defaultImageUrl = cloudinaryService.getProductDefault();

        if (existingProduct.getProductThumb().equals(defaultImageUrl)) {
            return defaultImageUrl;
        }

        try {
            cloudinaryService.deleteFile(existingProduct.getProductThumb());
        } catch (IOException e) {
            log.error("Failed to delete product image: {}", e.getMessage());
        }

        // Set the product's image back to the default
        existingProduct.setProductThumb(defaultImageUrl);
        productRepository.save(existingProduct);

        return defaultImageUrl;
    }
}
