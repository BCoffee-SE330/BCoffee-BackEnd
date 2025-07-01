package com.se330.coffee_shop_management_backend.service.catalogservice;

import com.se330.coffee_shop_management_backend.dto.request.catalog.CatalogCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.catalog.CatalogUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Catalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICatalogService {
    Catalog findByIdCatalog(Integer id);
    Page<Catalog> findAllCatalogs(Pageable pageable);
    Catalog createCatalog(CatalogCreateRequestDTO catalogCreateRequestDTO);
    Catalog updateCatalog(CatalogUpdateRequestDTO catalogUpdateRequestDTO);
    void deleteCatalog(Integer id);
    Page<Catalog> findChildCatalogs(Integer parentId, Pageable pageable);
}
