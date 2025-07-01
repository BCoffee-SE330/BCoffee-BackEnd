package com.se330.coffee_shop_management_backend.service.catalogservice.imp;

import com.se330.coffee_shop_management_backend.dto.request.catalog.CatalogCreateRequestDTO;
import com.se330.coffee_shop_management_backend.dto.request.catalog.CatalogUpdateRequestDTO;
import com.se330.coffee_shop_management_backend.entity.Catalog;
import com.se330.coffee_shop_management_backend.repository.CatalogRepository;
import com.se330.coffee_shop_management_backend.service.catalogservice.ICatalogService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImpCatalogService implements ICatalogService {

    private final CatalogRepository catalogRepository;

    public ImpCatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Catalog findByIdCatalog(Integer id) {
        return catalogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Catalog not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Catalog> findAllCatalogs(Pageable pageable) {
        return catalogRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Catalog createCatalog(CatalogCreateRequestDTO catalogCreateRequestDTO) {
        Catalog parentCatalog = null;

        if (catalogCreateRequestDTO.getParentCatalogId() != null) {
            parentCatalog = catalogRepository.findById(catalogCreateRequestDTO.getParentCatalogId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent Catalog not found with ID: " +
                            catalogCreateRequestDTO.getParentCatalogId()));
        }

        Catalog catalog = Catalog.builder()
                .name(catalogCreateRequestDTO.getName())
                .description(catalogCreateRequestDTO.getDescription())
                .parentCatalog(parentCatalog)
                .build();

        return catalogRepository.save(catalog);
    }

    @Override
    @Transactional
    public Catalog updateCatalog(CatalogUpdateRequestDTO catalogUpdateRequestDTO) {
        Catalog existingCatalog = catalogRepository.findById(catalogUpdateRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Catalog not found with ID: " +
                        catalogUpdateRequestDTO.getId()));

        Catalog parentCatalog = null;

        if (catalogUpdateRequestDTO.getParentCatalogId() != null) {
            // Ensure we're not creating a circular reference
            if (catalogUpdateRequestDTO.getParentCatalogId().equals(existingCatalog.getId())) {
                throw new IllegalArgumentException("Catalog cannot be its own parent");
            }

            parentCatalog = catalogRepository.findById(catalogUpdateRequestDTO.getParentCatalogId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent Catalog not found with ID: " +
                            catalogUpdateRequestDTO.getParentCatalogId()));
        }

        existingCatalog.setName(catalogUpdateRequestDTO.getName());
        existingCatalog.setDescription(catalogUpdateRequestDTO.getDescription());

        // Handle parent-child relationship changes
        if (existingCatalog.getParentCatalog() != null &&
                (parentCatalog == null || !existingCatalog.getParentCatalog().getId().equals(parentCatalog.getId()))) {
            existingCatalog.getParentCatalog().getChildCatalogs().remove(existingCatalog);
        }

        existingCatalog.setParentCatalog(parentCatalog);

        return catalogRepository.save(existingCatalog);
    }

    @Override
    @Transactional
    public void deleteCatalog(Integer id) {
        Catalog existingCatalog = catalogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Catalog not found with ID: " + id));

        if (existingCatalog.getParentCatalog() != null) {
            existingCatalog.getParentCatalog().getChildCatalogs().remove(existingCatalog);
            existingCatalog.setParentCatalog(null);
        }

        catalogRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Catalog> findChildCatalogs(Integer parentId, Pageable pageable) {
        Catalog parentCatalog = catalogRepository.findById(parentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent Catalog not found with ID: " + parentId));

        return catalogRepository.findAllByParentCatalog(parentCatalog, pageable);
    }
}