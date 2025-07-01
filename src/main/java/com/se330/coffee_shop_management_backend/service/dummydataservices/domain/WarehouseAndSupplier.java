package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.Supplier;
import com.se330.coffee_shop_management_backend.entity.Warehouse;
import com.se330.coffee_shop_management_backend.repository.SupplierRepository;
import com.se330.coffee_shop_management_backend.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WarehouseAndSupplier {
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;

    @Transactional
    public void create() {
        createWarehouse();
        createSupplier();
    }

    private void createWarehouse() {
        log.info("Creating warehouses...");

        List<Warehouse> warehouses = new ArrayList<>();

        warehouses.add(Warehouse.builder()
                .warehouseName("Kho Trung Tâm TP.HCM")
                .warehousePhone("028-3625-4789")
                .warehouseEmail("khott@bcoffee.com")
                .warehouseAddress("789 Đường Kho Vận, Quận Bình Tân, TP.HCM")
                .build());

        warehouses.add(Warehouse.builder()
                .warehouseName("Kho Nguyên Liệu Đà Lạt")
                .warehousePhone("0263-3575-8421")
                .warehouseEmail("khodalat@bcoffee.com")
                .warehouseAddress("45 Đường Đông Tây, TP. Đà Lạt, Lâm Đồng")
                .build());

        warehouses.add(Warehouse.builder()
                .warehouseName("Kho Xuất Khẩu Miền Đông")
                .warehousePhone("0251-3823-4567")
                .warehouseEmail("khomiendong@bcoffee.com")
                .warehouseAddress("123 Khu Công Nghiệp Biên Hòa, Đồng Nai")
                .build());

        warehouses.add(Warehouse.builder()
                .warehouseName("Kho Vận Miền Tây")
                .warehousePhone("0292-3845-6752")
                .warehouseEmail("khomientay@bcoffee.com")
                .warehouseAddress("456 Đường 30/4, Quận Ninh Kiều, Cần Thơ")
                .build());

        warehouseRepository.saveAll(warehouses);
        log.info("Created {} warehouses", warehouses.size());
    }

    private void createSupplier() {
        log.info("Creating suppliers...");

        List<Supplier> suppliers = new ArrayList<>();

        suppliers.add(Supplier.builder()
                .supplierName("Trung Nguyên Coffee")
                .supplierPhone("028-3512-7890")
                .supplierEmail("sales@trungnguyen.com.vn")
                .supplierAddress("82-84 Bùi Thị Xuân, Quận 1, TP.HCM")
                .build());

        suppliers.add(Supplier.builder()
                .supplierName("Vinamilk")
                .supplierPhone("028-5416-1226")
                .supplierEmail("orders@vinamilk.com.vn")
                .supplierAddress("10 Tân Trào, Quận 7, TP.HCM")
                .build());

        suppliers.add(Supplier.builder()
                .supplierName("Homefarm Organic")
                .supplierPhone("024-3762-8512")
                .supplierEmail("info@homefarm.vn")
                .supplierAddress("45 Nguyễn Thị Minh Khai, Quận Hai Bà Trưng, Hà Nội")
                .build());

        suppliers.add(Supplier.builder()
                .supplierName("Vĩnh Tiến Food")
                .supplierPhone("028-3839-5678")
                .supplierEmail("sales@vinhtienfood.vn")
                .supplierAddress("121 Lê Trọng Tấn, Quận Tân Phú, TP.HCM")
                .build());

        suppliers.add(Supplier.builder()
                .supplierName("Đà Lạt GAP")
                .supplierPhone("0263-3822-4578")
                .supplierEmail("contact@dalatgap.vn")
                .supplierAddress("28 Đường 3/2, Phường 1, TP. Đà Lạt")
                .build());

        suppliers.add(Supplier.builder()
                .supplierName("Phú Sỹ Import")
                .supplierPhone("028-3954-1265")
                .supplierEmail("info@phusyimport.com")
                .supplierAddress("56 Nguyễn Đình Chiểu, Quận 1, TP.HCM")
                .build());

        suppliers.add(Supplier.builder()
                .supplierName("Kim Long Packaging")
                .supplierPhone("0251-3886-1234")
                .supplierEmail("sales@kimlong.vn")
                .supplierAddress("Khu Công Nghiệp Amata, Biên Hòa, Đồng Nai")
                .build());

        suppliers.add(Supplier.builder()
                .supplierName("Perfetto Italia")
                .supplierPhone("028-3823-7819")
                .supplierEmail("export@perfetto.it")
                .supplierAddress("182 Pasteur, Quận 3, TP.HCM")
                .build());

        suppliers.add(Supplier.builder()
                .supplierName("Biên Hòa Sugar")
                .supplierPhone("0251-3836-7890")
                .supplierEmail("sales@bhs.vn")
                .supplierAddress("Khu Công Nghiệp Biên Hòa, Đồng Nai")
                .build());

        suppliers.add(Supplier.builder()
                .supplierName("Phúc Long Tea")
                .supplierPhone("028-3821-5789")
                .supplierEmail("wholesale@phuclong.com.vn")
                .supplierAddress("42A Nguyễn Huệ, Quận 1, TP.HCM")
                .build());

        supplierRepository.saveAll(suppliers);
        log.info("Created {} suppliers", suppliers.size());
    }
}
