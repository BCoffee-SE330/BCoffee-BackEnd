package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductCategoryUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductCategoryCreateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Catalog;
import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import com.se330.coffee_shop_management_backend.repository.CatalogRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.service.productservices.IProductCategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImpProductCategoryService implements IProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final CatalogRepository catalogRepository;

    public ImpProductCategoryService(
            ProductCategoryRepository productCategoryRepository,
            CatalogRepository catalogRepository
    ) {
        this.productCategoryRepository = productCategoryRepository;
        this.catalogRepository = catalogRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public ProductCategory findByIdProductCategory(UUID id) {
        return productCategoryRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductCategory> findAllProductCategories(Pageable pageable) {
        return productCategoryRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductCategory> findAllProductCategoriesByCatalogId(Integer catalogId, Pageable pageable) {
        return productCategoryRepository.findAllByCatalog_Id(catalogId, pageable);
    }

    @Override
    @Transactional
    public ProductCategory createProductCategory(ProductCategoryCreateRequestDTO productCategoryRequestDTO) {
        Catalog existingCatalog = catalogRepository.findById(productCategoryRequestDTO.getCatalogId())
                .orElseThrow(() -> new EntityNotFoundException("Catalog not found with ID: " + productCategoryRequestDTO.getCatalogId()));

        return productCategoryRepository.save(
                ProductCategory.builder()
                        .categoryName(productCategoryRequestDTO.getCategoryName())
                        .categoryDescription(productCategoryRequestDTO.getCategoryDescription())
                        .catalog(existingCatalog)
                        .build()
        );
    }

    @Override
    @Transactional
    public ProductCategory updateProductCategory(ProductCategoryUpdateRequestDTO productCategoryRequestDTO) {
        ProductCategory existingCategory = productCategoryRepository.findById(productCategoryRequestDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + productCategoryRequestDTO.getCategoryId()));

        Catalog existingCatalog = catalogRepository.findById(productCategoryRequestDTO.getCatalogId())
                .orElseThrow(() -> new EntityNotFoundException("Catalog not found with ID: " + productCategoryRequestDTO.getCatalogId()));

        existingCategory.setCategoryName(productCategoryRequestDTO.getCategoryName());
        existingCategory.setCategoryDescription(productCategoryRequestDTO.getCategoryDescription());
        existingCategory.setCatalog(existingCatalog);

        return productCategoryRepository.save(existingCategory);
    }

    @Override
    @Transactional
    public void deleteProductCategory(UUID id) {
        ProductCategory existingProduct = productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));

        if (existingProduct.getCatalog() != null) {
            Catalog catalog = existingProduct.getCatalog();
            catalog.getCategories().remove(existingProduct);
            catalogRepository.save(catalog);
        }

        productCategoryRepository.deleteById(id);
    }
}
