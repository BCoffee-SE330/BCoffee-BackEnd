package com.se330.coffee_shop_management_backend.controller.productcontrollers;

import com.se330.coffee_shop_management_backend.dto.request.product.NewProductCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.ErrorResponse;
import com.se330.coffee_shop_management_backend.dto.response.PageResponse;
import com.se330.coffee_shop_management_backend.dto.response.SingleResponse;
import com.se330.coffee_shop_management_backend.dto.response.product.BestSellingProductResponseDTO;
import com.se330.coffee_shop_management_backend.dto.response.product.ProductResponseDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.service.favoritedrinkservices.IFavoriteDrinkService;
import com.se330.coffee_shop_management_backend.service.productservices.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

import static com.se330.coffee_shop_management_backend.util.Constants.SECURITY_SCHEME_NAME;
import static com.se330.coffee_shop_management_backend.util.CreatePageHelper.createPageable;

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final IProductService productService;
    private final IFavoriteDrinkService favoriteDrinkService;

    public ProductController(IProductService productService, IFavoriteDrinkService favoriteDrinkService) {
        this.productService = productService;
        this.favoriteDrinkService = favoriteDrinkService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get product detail",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved product",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID format",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ProductResponseDTO>> findById(@PathVariable UUID id) {
        ProductResponseDTO product = ProductResponseDTO.convert(productService.findByIdProduct(id));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Product retrieved successfully",
                        product
                )
        );
    }

    @GetMapping("/user/{id}")
    @Operation(
            summary = "Get product detail with favorite status for user",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved product with favorite status",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID format",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ProductResponseDTO>> userFindById(@PathVariable UUID id) {
        Product product = productService.findByIdProduct(id);
        boolean isFavorite = favoriteDrinkService.isDrinkFavoritedByUser(id);
        ProductResponseDTO productResponseDTO = ProductResponseDTO.convert(product, isFavorite);
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Product retrieved successfully",
                        productResponseDTO
                )
        );
    }

    @GetMapping("/by-category/{categoryId}")
    @Operation(
            summary = "Get all products by category with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved products for the specified category",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<ProductResponseDTO>> findAllProductsByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Product> productPage = productService.findAllProductsByCategory(categoryId, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Products for category retrieved successfully",
                        ProductResponseDTO.convert(productPage.getContent()),
                        new PageResponse.PagingResponse(
                                productPage.getNumber(),
                                productPage.getSize(),
                                productPage.getTotalElements(),
                                productPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all products with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved product list",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<ProductResponseDTO>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Product> productPage = productService.findAllProducts(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Products retrieved successfully",
                        ProductResponseDTO.convert(productPage.getContent()),
                        new PageResponse.PagingResponse(
                                productPage.getNumber(),
                                productPage.getSize(),
                                productPage.getTotalElements(),
                                productPage.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Create new product",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ProductResponseDTO>> create(
            @RequestBody ProductCreateRequestDTO dto,
            @RequestParam(value = "product-image", required = false) MultipartFile productImage
    ) throws Exception {
        if (productImage != null) {
            ProductResponseDTO product = ProductResponseDTO.convert(productService.createProductWithImage(dto, productImage));
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new SingleResponse<>(
                            HttpStatus.CREATED.value(),
                            "Product created successfully with image",
                            product
                    )
            );
        }
        ProductResponseDTO product = ProductResponseDTO.convert(productService.createProduct(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Product created successfully",
                        product
                )
        );
    }

    @PatchMapping("/")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Update product",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ProductResponseDTO>> update(@RequestBody ProductUpdateRequestDTO dto) {
        ProductResponseDTO product = ProductResponseDTO.convert(productService.updateProduct(dto));
        return ResponseEntity.ok(
                new SingleResponse<>(
                        HttpStatus.OK.value(),
                        "Product updated successfully",
                        product
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Delete product",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Product deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/image/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Upload product image",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Image uploaded successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or empty file",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> uploadProductImage(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        try {
            String imageUrl = productService.uploadProductImage(id, file);
            return ResponseEntity.ok(
                    new SingleResponse<>(
                            HttpStatus.OK.value(),
                            "Product image uploaded successfully",
                            Map.of("url", imageUrl)
                    )
            );
        } catch (Exception e) {
            log.info("Error uploading product image: {}", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading product image");
    }

    @DeleteMapping("/image/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Delete product image",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Image deleted successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> deleteProductImage(@PathVariable UUID id) {
        try {
            String defaultImageUrl = productService.deleteProductImage(id);
            return ResponseEntity.ok(
                    new SingleResponse<>(
                            HttpStatus.OK.value(),
                            "Product image deleted successfully",
                            Map.of("url", defaultImageUrl)
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product image");
        }
    }

    @GetMapping("/best-selling")
    @Operation(
            summary = "Get all best selling products with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved best selling products",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findAllBestSellingProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalSold") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findAllBestSellingProducts(pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Best selling products retrieved successfully",
                        productsPage.getContent(),
                        new PageResponse.PagingResponse(
                                productsPage.getNumber(),
                                productsPage.getSize(),
                                productsPage.getTotalElements(),
                                productsPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/best-selling/year/{year}")
    @Operation(
            summary = "Get best selling products by year with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved best selling products for specified year",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByYear(
            @PathVariable int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalSold") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByYear(year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Best selling products for year " + year + " retrieved successfully",
                        productsPage.getContent(),
                        new PageResponse.PagingResponse(
                                productsPage.getNumber(),
                                productsPage.getSize(),
                                productsPage.getTotalElements(),
                                productsPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/best-selling/year/{year}/month/{month}")
    @Operation(
            summary = "Get best selling products by month and year with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved best selling products for specified month and year",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByMonthAndYear(
            @PathVariable int month,
            @PathVariable int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalSold") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByMonthAndYear(month, year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Best selling products for month " + month + " year " + year + " retrieved successfully",
                        productsPage.getContent(),
                        new PageResponse.PagingResponse(
                                productsPage.getNumber(),
                                productsPage.getSize(),
                                productsPage.getTotalElements(),
                                productsPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/best-selling/year/{year}/month/{month}/day/{day}")
    @Operation(
            summary = "Get best selling products by day, month and year with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved best selling products for specified day, month and year",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByDayAndMonthAndYear(
            @PathVariable int day,
            @PathVariable int month,
            @PathVariable int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalSold") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByDayAndMonthAndYear(day, month, year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Best selling products for day " + day + " month " + month + " year " + year + " retrieved successfully",
                        productsPage.getContent(),
                        new PageResponse.PagingResponse(
                                productsPage.getNumber(),
                                productsPage.getSize(),
                                productsPage.getTotalElements(),
                                productsPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/best-selling/branch/{branchId}")
    @Operation(
            summary = "Get best selling products by branch with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved best selling products for specified branch",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByBranch(
            @PathVariable UUID branchId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalSold") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByBranch(branchId, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Best selling products for branch retrieved successfully",
                        productsPage.getContent(),
                        new PageResponse.PagingResponse(
                                productsPage.getNumber(),
                                productsPage.getSize(),
                                productsPage.getTotalElements(),
                                productsPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/best-selling/branch/{branchId}/year/{year}")
    @Operation(
            summary = "Get best selling products by branch and year with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved best selling products for specified branch and year",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByBranchAndYear(
            @PathVariable UUID branchId,
            @PathVariable int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalSold") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByBranchAndYear(branchId, year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Best selling products for branch and year " + year + " retrieved successfully",
                        productsPage.getContent(),
                        new PageResponse.PagingResponse(
                                productsPage.getNumber(),
                                productsPage.getSize(),
                                productsPage.getTotalElements(),
                                productsPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/best-selling/branch/{branchId}/year/{year}/month/{month}")
    @Operation(
            summary = "Get best selling products by branch, month and year with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved best selling products for specified branch, month and year",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByBranchAndMonthAndYear(
            @PathVariable UUID branchId,
            @PathVariable int month,
            @PathVariable int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalSold") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByBranchAndMonthAndYear(branchId, month, year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Best selling products for branch, month " + month + " and year " + year + " retrieved successfully",
                        productsPage.getContent(),
                        new PageResponse.PagingResponse(
                                productsPage.getNumber(),
                                productsPage.getSize(),
                                productsPage.getTotalElements(),
                                productsPage.getTotalPages()
                        )
                )
        );
    }

    @GetMapping("/best-selling/branch/{branchId}/year/{year}/month/{month}/day/{day}")
    @Operation(
            summary = "Get best selling products by branch, day, month and year with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved best selling products for specified branch, day, month and year",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<BestSellingProductResponseDTO>> findBestSellingProductsByBranchAndDayAndMonthAndYear(
            @PathVariable UUID branchId,
            @PathVariable int day,
            @PathVariable int month,
            @PathVariable int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "totalSold") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<BestSellingProductResponseDTO> productsPage = productService.findBestSellingProductsByBranchAndDayAndMonthAndYear(branchId, day, month, year, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Best selling products for branch, day " + day + ", month " + month + " and year " + year + " retrieved successfully",
                        productsPage.getContent(),
                        new PageResponse.PagingResponse(
                                productsPage.getNumber(),
                                productsPage.getSize(),
                                productsPage.getTotalElements(),
                                productsPage.getTotalPages()
                        )
                )
        );
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Create new product with variants and recipes",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product created successfully with variants",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ProductResponseDTO>> createNew(@RequestBody NewProductCreateRequestDTO dto) {
        ProductResponseDTO product = ProductResponseDTO.convert(productService.createProductNew(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Product created successfully with variants",
                        product
                )
        );
    }

    @PostMapping(value = "/new-with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(
            summary = "Create new product with variants, recipes and image",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product created successfully with variants and image",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<SingleResponse<ProductResponseDTO>> createNewWithImage(
            @RequestPart("product") NewProductCreateRequestDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile file
    ) throws Exception {
        ProductResponseDTO product = ProductResponseDTO.convert(productService.createProductNewWithImage(dto, file));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SingleResponse<>(
                        HttpStatus.CREATED.value(),
                        "Product created successfully with variants and image",
                        product
                )
        );
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search products by keyword with pagination",
            security = @SecurityRequirement(name = SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved matching products",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<PageResponse<ProductResponseDTO>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "desc") String sortType,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Integer offset = (page - 1) * limit;
        Pageable pageable = createPageable(page, limit, offset, sortType, sortBy);
        Page<Product> productPage = productService.searchProducts(keyword, pageable);

        return ResponseEntity.ok(
                new PageResponse<>(
                        HttpStatus.OK.value(),
                        "Products matching search criteria retrieved successfully",
                        ProductResponseDTO.convert(productPage.getContent()),
                        new PageResponse.PagingResponse(
                                productPage.getNumber() + 1,
                                productPage.getSize(),
                                productPage.getTotalElements(),
                                productPage.getTotalPages()
                        )
                )
        );
    }
}