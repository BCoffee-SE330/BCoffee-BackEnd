package com.se330.coffee_shop_management_backend.service.supplierservices;

import com.se330.coffee_shop_management_backend.dto.request.supplier.SupplierCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.supplier.SupplierUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ISupplierService {
    Supplier findByIdSupplier(UUID id);
    Page<Supplier> findAllSuppliers(Pageable pageable);
    Supplier createSupplier(SupplierCreateRequestDTO supplierCreateRequestDTO);
    Supplier updateSupplier(SupplierUpdateRequestDTO supplierUpdateRequestDTO);
    void deleteSupplier(UUID id);
}