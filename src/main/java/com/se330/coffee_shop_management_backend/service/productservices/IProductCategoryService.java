package com.se330.coffee_shop_management_backend.service.productservices;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductCategoryUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductCategoryCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IProductCategoryService {
    ProductCategory findByIdProductCategory(UUID id);
    Page<ProductCategory> findAllProductCategories(Pageable pageable);
    Page<ProductCategory> findAllProductCategoriesByCatalogId(Integer catalogId, Pageable pageable);
    ProductCategory createProductCategory(ProductCategoryCreateRequestDTO productCategoryRequestDTO);
    ProductCategory updateProductCategory(ProductCategoryUpdateRequestDTO productCategoryRequestDTO);
    void deleteProductCategory(UUID id);
}
