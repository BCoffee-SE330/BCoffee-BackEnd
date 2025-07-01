package com.se330.coffee_shop_management_backend.service.productservices;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductVariantCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductVariantUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IProductVariantService {
    ProductVariant findByIdProductVariant(UUID id);
    Page<ProductVariant> findAllProductVariants(Pageable pageable);
    Page<ProductVariant> findAllProductVariantsByProductId(UUID productId, Pageable pageable);
    ProductVariant createProductVariant(ProductVariantCreateRequestDTO productVariantCreateRequestDTO);
    ProductVariant updateProductVariant(ProductVariantUpdateRequestDTO productVariantUpdateRequestDTO);
    void deleteProductVariant(UUID id);
}
