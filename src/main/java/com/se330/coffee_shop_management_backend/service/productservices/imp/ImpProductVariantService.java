package com.se330.coffee_shop_management_backend.service.productservices.imp;

import com.se330.coffee_shop_management_backend.dto.request.product.ProductVariantCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductVariantUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.service.productservices.IProductVariantService;
import com.se330.coffee_shop_management_backend.util.CreateSlug;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ImpProductVariantService implements IProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    public ImpProductVariantService(
            ProductVariantRepository productVariantRepository,
            ProductRepository productRepository
    ) {
        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public ProductVariant findByIdProductVariant(UUID id) {
        return productVariantRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductVariant> findAllProductVariants(Pageable pageable) {
        return productVariantRepository.findAll(pageable);
    }

    @Override
    public Page<ProductVariant> findAllProductVariantsByProductId(UUID productId, Pageable pageable) {
        return productVariantRepository.findAllByProduct_Id(productId, pageable);
    }

    @Transactional
    @Override
    public ProductVariant createProductVariant(ProductVariantCreateRequestDTO productVariantCreateRequestDTO) {
        Product product = productRepository.findById(productVariantCreateRequestDTO.getProduct())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productVariantCreateRequestDTO.getProduct()));

        return productVariantRepository.save(
                ProductVariant.builder()
                        .variantTierIdx(productVariantCreateRequestDTO.getVariantTierIdx())
                        .variantDefault(productVariantCreateRequestDTO.getVariantDefault())
                        .variantPrice(productVariantCreateRequestDTO.getVariantPrice())
                        .product(product)
                        .variantSlug(CreateSlug.createSlug(productVariantCreateRequestDTO.getVariantTierIdx()))
                        .variantSort(productVariantCreateRequestDTO.getVariantSort())
                        .variantIsPublished(true)
                        .variantIsDeleted(false)
                        .build()
        );
    }

    @Transactional
    @Override
    public ProductVariant updateProductVariant(ProductVariantUpdateRequestDTO productVariantUpdateRequestDTO) {

        ProductVariant existingVariant = productVariantRepository.findById(productVariantUpdateRequestDTO.getVariantId())
                .orElseThrow(() -> new EntityNotFoundException("Product Variant not found with ID: " + productVariantUpdateRequestDTO.getVariantId()));

        Product product = productRepository.findById(productVariantUpdateRequestDTO.getProduct())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productVariantUpdateRequestDTO.getProduct()));

        if (existingVariant.getProduct() != null) {
            existingVariant.getProduct().getProductVariants().remove(existingVariant);
            existingVariant.setProduct(product);
            product.getProductVariants().add(existingVariant);
        }

        existingVariant.setVariantTierIdx(productVariantUpdateRequestDTO.getVariantTierIdx());
        existingVariant.setVariantDefault(productVariantUpdateRequestDTO.getVariantDefault());
        existingVariant.setVariantSlug(CreateSlug.createSlug(productVariantUpdateRequestDTO.getVariantTierIdx()));
        existingVariant.setVariantSort(productVariantUpdateRequestDTO.getVariantSort());
        existingVariant.setVariantPrice(productVariantUpdateRequestDTO.getVariantPrice());
        existingVariant.setVariantIsPublished(productVariantUpdateRequestDTO.getVariantIsPublished());
        existingVariant.setVariantIsDeleted(productVariantUpdateRequestDTO.getVariantIsDeleted());

        return productVariantRepository.save(existingVariant);
    }

    @Transactional
    @Override
    public void deleteProductVariant(UUID id) {
        ProductVariant existingProductVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product Variant not found with ID: " + id));

        if (existingProductVariant.getProduct() != null) {
            existingProductVariant.getProduct().getProductVariants().remove(existingProductVariant);
            existingProductVariant.setProduct(null);
        }

        productVariantRepository.deleteById(id);
    }
}
