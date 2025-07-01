package com.se330.coffee_shop_management_backend.service.productservices;

import com.se330.coffee_shop_management_backend.dto.request.product.NewProductCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.product.ProductUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.response.product.BestSellingProductResponseDTO;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IProductService {
    Product findByIdProduct(UUID id);
    Page<Product> findAllProducts(Pageable pageable);
    Page<Product> findAllProductsByCategory(UUID categoryId, Pageable pageable);
    Product createProduct(ProductCreateRequestDTO productCreateRequestDTO);
    Product createProductWithImage(ProductCreateRequestDTO productCreateRequestDTO, MultipartFile file) throws Exception;
    Product createProductNew(NewProductCreateRequestDTO newProductCreateRequestDTO);
    Product createProductNewWithImage(NewProductCreateRequestDTO newProductCreateRequestDTO, MultipartFile file) throws Exception;
    Product updateProduct(ProductUpdateRequestDTO productUpdateRequestDTORequestDTO);
    void deleteProduct(UUID id);
    Page<BestSellingProductResponseDTO> findAllBestSellingProducts(Pageable pageable);
    Page<BestSellingProductResponseDTO> findBestSellingProductsByYear(int year, Pageable pageable);
    Page<BestSellingProductResponseDTO> findBestSellingProductsByMonthAndYear(int month, int year, Pageable pageable);
    Page<BestSellingProductResponseDTO> findBestSellingProductsByDayAndMonthAndYear(int day, int month, int year, Pageable pageable);
    Page<BestSellingProductResponseDTO> findBestSellingProductsByBranch(UUID branchId, Pageable pageable);
    Page<BestSellingProductResponseDTO> findBestSellingProductsByBranchAndYear(UUID branchId, int year, Pageable pageable);
    Page<BestSellingProductResponseDTO> findBestSellingProductsByBranchAndMonthAndYear(UUID branchId, int month, int year, Pageable pageable);
    Page<BestSellingProductResponseDTO> findBestSellingProductsByBranchAndDayAndMonthAndYear(UUID branchId, int day, int month, int year, Pageable pageable);

    Page<Product> searchProducts(String keyword, Pageable pageable);

    String uploadProductImage(UUID productId, MultipartFile file) throws Exception;
    String deleteProductImage(UUID productId) throws Exception;
}
