package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.Catalog;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductCategory;
import com.se330.coffee_shop_management_backend.repository.CatalogRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductCategoryRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParentProduct {

    private final CatalogRepository catalogRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void create() throws BindException {
        createCataLogAndCategory();
        createProduct();
    }

    private void createCataLogAndCategory() throws BindException {
        catalogRepository.save(
            Catalog.builder()
                .name("Some shit")
                .description("Ignore this")
                .build()
        );

        List<ProductCategory> categories = new ArrayList<>();

        categories.add(ProductCategory.builder()
                .categoryName("Cà phê")
                .categoryDescription("Sự kết hợp hoàn hảo giữa hạt cà phê Robusta & Arabica thượng hạng được trồng trên những vùng cao nguyên Việt Nam màu mỡ, qua những bí quyết rang xay độc đáo, BCoffee chúng tôi tự hào giới thiệu những dòng sản phẩm Cà phê mang hương vị đậm đà và tinh tế.")
                .build());

        categories.add(ProductCategory.builder()
                .categoryName("Trà")
                .categoryDescription("Từ những lá trà tươi ngon được tuyển chọn kỹ lưỡng từ các vùng trà nổi tiếng, BCoffee mang đến những thức uống trà đầy hương sắc, từ vị thanh mát của trà sen, hương thơm quyến rũ của trà hoa lài đến vị chát dịu của trà đào, tất cả đều được chế biến theo công thức độc quyền tạo nên trải nghiệm thưởng thức tuyệt vời.")
                .build());

        categories.add(ProductCategory.builder()
                .categoryName("Freeze")
                .categoryDescription("Sảng khoái và năng động với dòng thức uống đá xay mát lạnh. BCoffee tự hào giới thiệu những ly Freeze đặc biệt với công thức độc đáo, kết hợp hài hòa giữa đá xay, sữa và các hương vị tự nhiên, mang đến trải nghiệm giải nhiệt tuyệt vời cho những ngày nắng nóng hay khi cần tỉnh táo làm việc.")
                .build());

        categories.add(ProductCategory.builder()
                .categoryName("Đồ ăn")
                .categoryDescription("Những món ăn nhẹ được chế biến từ nguyên liệu tươi ngon hàng ngày tại BCoffee. Từ bánh mì giòn rụm, bánh ngọt thơm béo đến các loại sandwich đầy dinh dưỡng, tất cả đều được làm thủ công để đảm bảo chất lượng và hương vị, là người bạn đồng hành hoàn hảo cho thức uống yêu thích của bạn.")
                .build());

        categories.add(ProductCategory.builder()
                .categoryName("Khác")
                .categoryDescription("BCoffee không ngừng đổi mới với đa dạng sản phẩm độc đáo, từ những món đồ uống theo mùa, thức uống chức năng tốt cho sức khỏe đến những hương vị fusion kết hợp văn hóa ẩm thực Đông - Tây. Đây là không gian sáng tạo, nơi chúng tôi mang đến những trải nghiệm mới mẻ và bất ngờ cho thực khách.")
                .build());

        productCategoryRepository.saveAll(categories);
    }

    private void createProduct() {
        log.info("Creating products...");

        // Get product categories
        List<ProductCategory> categories = productCategoryRepository.findAll();
        if (categories.isEmpty()) {
            log.error("Cannot create products: No product categories found");
            return;
        }

        ProductCategory coffeeCategory = categories.stream()
                .filter(c -> c.getCategoryName().equals("Cà phê"))
                .findFirst()
                .orElse(null);

        ProductCategory freezeCategory = categories.stream()
                .filter(c -> c.getCategoryName().equals("Freeze"))
                .findFirst()
                .orElse(null);

        ProductCategory teaCategory = categories.stream()
                .filter(c -> c.getCategoryName().equals("Trà"))
                .findFirst()
                .orElse(null);

        ProductCategory foodCategory = categories.stream()
                .filter(c -> c.getCategoryName().equals("Đồ ăn"))
                .findFirst()
                .orElse(null);

        ProductCategory otherCategory = categories.stream()
                .filter(c -> c.getCategoryName().equals("Khác"))
                .findFirst()
                .orElse(null);

        List<Product> products = new ArrayList<>();

        // Create coffee products
        if (coffeeCategory != null) {
            Map<String, String> coffeeNameAndUrl = Map.of(
                    "PHINDI KEM SỮA", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146735/270_crop_HLC_New_logo_5.1_Products__PHINDI_KEM_SUA_km7nao.jpg",
                    "PHINDI HẠNH NHÂN", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146735/270_crop_HLC_New_logo_5.1_Products__PHINDI_HANH_NHAN_how1se.jpg",
                    "PHINDI CHOCO", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146734/270_crop_HLC_New_logo_5.1_Products__PHINDI_CHOCO_rxh9te.jpg",
                    "MOCHA MACHIATO", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146733/270_crop_HLC_New_logo_5.1_Products__MOCHA_ipeik6.jpg",
                    "LATTE", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146733/270_crop_HLC_New_logo_5.1_Products__LATTE_1_trkuuy.jpg",
                    "CARAMEL MACHIATO", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146732/270_crop_HLC_New_logo_5.1_Products__CARAMEL_MACCHIATTO_u3fsu0.jpg",
                    "ESPRESSO", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146732/270_crop_HLC_New_logo_5.1_Products__EXPRESSO_kvtjbl.jpg",
                    "AMERICANO", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146731/270_crop_HLC_New_logo_5.1_Products__AMERICANO_NONG_tbhucn.jpg",
                    "CAPUCHINO", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146731/270_crop_HLC_New_logo_5.1_Products__CAPPUCINO_lsdfzy.jpg",
                    "BẠC XĨU ĐÁ", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146731/270_crop_HLC_New_logo_5.1_Products__BAC_XIU_ehyflh.jpg"
            );

            coffeeNameAndUrl.forEach((name, url) -> {
                String slug = createSlug(name);
                products.add(Product.builder()
                        .productName(name)
                        .productThumb(url)
                        .productDescription(generateCoffeeDescription(name))
                        .productPrice(new BigDecimal(String.valueOf(10000 + (new Random().nextInt(20) * 1000))))
                        .productSlug(slug)
                        .productCommentCount(0)
                        .productRatingsAverage(BigDecimal.ZERO)
                        .productIsPublished(true)
                        .productIsDeleted(false)
                        .productCategory(coffeeCategory)
                        .build());
            });
        }

        // Create freeze products
        if (freezeCategory != null) {
            Map<String, String> freezeNameAndUrl = Map.of(
                    "FREEZE TRÀ XANH", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146750/270_crop_HLC_New_logo_5.1_Products__FREEZE_TRA_XANH_gri77r.jpg",
                    "FREEZE CHOCO", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146749/270_crop_HLC_New_logo_5.1_Products__FREEZE_CHOCO_jngt5a.jpg",
                    "COOKIES & CREAM", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146749/270_crop_HLC_New_logo_5.1_Products__COOKIES_FREEZE_qcb4y7.jpg",
                    "CLASSIC FREEZE PHINDI", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146748/270_crop_HLC_New_logo_5.1_Products__CLASSIC_FREEZE_PHINDI_ky1msd.jpg",
                    "CARAMEL FREEZE PHINDI", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146747/270_crop_HLC_New_logo_5.1_Products__CARAMEL_FREEZE_PHINDI_co5reu.jpg"
            );

            freezeNameAndUrl.forEach((name, url) -> {
                String slug = createSlug(name);
                products.add(Product.builder()
                        .productName(name)
                        .productThumb(url)
                        .productDescription(generateFreezeDescription(name))
                        .productPrice(new BigDecimal(String.valueOf(19000 + (new Random().nextInt(15) * 1000))))
                        .productSlug(slug)
                        .productCommentCount(0)
                        .productRatingsAverage(BigDecimal.ZERO)
                        .productIsPublished(true)
                        .productIsDeleted(false)
                        .productCategory(freezeCategory)
                        .build());
            });
        }

        // Create tea products
        if (teaCategory != null) {
            Map<String, String> teaNameAndUrl = Map.of(
                    "TRÀ SEN VÀNG (HẠT SEN)", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146739/270_crop_HLC_New_logo_5.1_Products__TSV_zctjwg.jpg",
                    "TRÀ XANH ĐẬU ĐỎ", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146738/270_crop_HLC_New_logo_5.1_Products__TRA_XANH_DAU_DO_hfvuti.jpg",
                    "TRÀ THẠCH ĐÀO", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146738/270_crop_HLC_New_logo_5.1_Products__TRA_THANH_DAO-09_odeagc.jpg",
                    "TRÀ THANH ĐÀO", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146737/270_crop_HLC_New_logo_5.1_Products__TRA_THANH_DAO-08_edfqpl.jpg",
                    "TRÀ SEN VÀNG (CỦ NĂNG)", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146736/270_crop_HLC_New_logo_5.1_Products__TRA_SEN_VANG_CU_NANG_nsvaog.jpg",
                    "TRÀ THẠCH VẢI", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146736/270_crop_HLC_New_logo_5.1_Products__TRA_TACH_VAI_norrzk.jpg"
            );

            teaNameAndUrl.forEach((name, url) -> {
                String slug = createSlug(name);
                products.add(Product.builder()
                        .productName(name)
                        .productThumb(url)
                        .productDescription(generateTeaDescription(name))
                        .productPrice(new BigDecimal(String.valueOf(20000 + (new Random().nextInt(18) * 1000))))
                        .productSlug(slug)
                        .productCommentCount(0)
                        .productRatingsAverage(BigDecimal.ZERO)
                        .productIsPublished(true)
                        .productIsDeleted(false)
                        .productCategory(teaCategory)
                        .build());
            });
        }

        // Create food products
        if (foodCategory != null) {
            Map<String, String> foodNameAndUrl = Map.of(
                    "SET BÁNH NGỌT TIRAMISU", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146744/270_crop_CROP_Cake_2png_zfuter.jpg",
                    "SET BÁNH NGỌT MUFFIN", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146743/270_crop_CROP_Cake_3png_jwqfqe.jpg",
                    "SET BÁNH NGỌT CHOCO", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146742/270_crop_CROP_Cake_1png_qwwsai.jpg",
                    "BÁNH MÌ TRUYỀN THỐNG", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146742/270_crop_CROP_Banh_Mi.png_pzqpms.jpg",
                    "BÁNH MÌ QUE PATE", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146741/270_crop_BMQ_Pate_hw4cmi.png",
                    "BÁNH MÌ QUE GÀ PHÔ MAI", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146740/270_crop_BMQ_Ga_Pho_Mai_nq2dhi.png",
                    "BÁNH MÌ QUE BÒ PHÔ MAI", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146740/270_crop_BMQ_Bo_Pho_Mai_ndwqvk.png",
                    "CROISSANT", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146741/270_crop_Croissant_geya4u.png"
            );

            foodNameAndUrl.forEach((name, url) -> {
                String slug = createSlug(name);
                products.add(Product.builder()
                        .productName(name)
                        .productThumb(url)
                        .productDescription(generateFoodDescription(name))
                        .productPrice(new BigDecimal(String.valueOf(20000 + (new Random().nextInt(25) * 1000))))
                        .productSlug(slug)
                        .productCommentCount(0)
                        .productRatingsAverage(BigDecimal.ZERO)
                        .productIsPublished(true)
                        .productIsDeleted(false)
                        .productCategory(foodCategory)
                        .build());
            });
        }

        // Create other products
        if (otherCategory != null) {
            Map<String, String> otherNameAndUrl = Map.of(
                    "GÓI CÀ PHÊ TRUYỀN THỐNG 200G", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146747/CAFE_TRUYEN_THONG_200_G_xidm7e.jpg",
                    "GÓI CÀ PHÊ TRUYỀN THỐNG 1KG", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146746/270_crop_RG-TRADITIONAL-1kg-5.1_ecm6ff.png",
                    "LON CÀ PHÊ ĐEN 185ml/6 LON", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146746/CAFE_DEN_LON_185ml_LOC_6_ccdkon.jpg",
                    "LON CÀ PHÊ SỮA 185ml/6 LON", "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749146744/270_crop_CA_PHE_SUA_LON_185ml_LOC_6_v3_s9w3nh.jpg"
            );

            otherNameAndUrl.forEach((name, url) -> {
                String slug = createSlug(name);
                products.add(Product.builder()
                        .productName(name)
                        .productThumb(url)
                        .productDescription(generateOtherDescription(name))
                        .productPrice(new BigDecimal(String.valueOf(30000 + (new Random().nextInt(20) * 5000))))
                        .productSlug(slug)
                        .productCommentCount(0)
                        .productRatingsAverage(BigDecimal.ZERO)
                        .productIsPublished(true)
                        .productIsDeleted(false)
                        .productCategory(otherCategory)
                        .build());
            });
        }

        productRepository.saveAll(products);
        log.info("Created {} products", products.size());
    }

    private String createSlug(String productName) {
        return productName.toLowerCase()
                .replaceAll("đ", "d")
                .replaceAll("[áàảãạăắằẳẵặâấầẩẫậ]", "a")
                .replaceAll("[éèẻẽẹêếềểễệ]", "e")
                .replaceAll("[íìỉĩị]", "i")
                .replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o")
                .replaceAll("[úùủũụưứừửữự]", "u")
                .replaceAll("[ýỳỷỹỵ]", "y")
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("[\\s]", "-");
    }

    private String generateCoffeeDescription(String name) {
        if (name.contains("PHINDI")) {
            return "Sự kết hợp độc đáo giữa cà phê đậm đà và " + (name.contains("KEM SỮA") ? "kem sữa mịn màng" :
                    name.contains("HẠNH NHÂN") ? "hương hạnh nhân thơm ngọt" : "sô-cô-la đắng ngọt") +
                    ", tạo nên hương vị đặc trưng của BCoffee mà bạn không thể tìm thấy ở bất kỳ đâu khác.";
        } else if (name.contains("LATTE")) {
            return "Cà phê Espresso đậm đà hòa quyện cùng sữa tươi béo ngậy và bọt sữa mịn màng, tạo nên một ly Latte hoàn hảo đậm chất Ý từ BCoffee.";
        } else if (name.contains("AMERICANO")) {
            return "Sự kết hợp giữa Espresso đậm đà và nước nóng tạo nên ly Americano với hương vị cân bằng, là lựa chọn hoàn hảo cho những ai yêu thích vị đắng thuần túy của cà phê.";
        } else if (name.contains("ESPRESSO")) {
            return "Tinh túy từ những hạt cà phê Arabica và Robusta thượng hạng, được chiết xuất hoàn hảo để tạo nên tách Espresso đậm đà với lớp crema vàng óng đặc trưng.";
        } else if (name.contains("BẠC XĨU")) {
            return "Thức uống đậm chất Việt Nam với sự kết hợp giữa cà phê và sữa đặc, tạo nên ly Bạc Xỉu với vị ngọt béo đặc trưng và hương thơm quyến rũ.";
        } else {
            return "Một kiệt tác từ BCoffee, kết hợp giữa cà phê espresso đậm đà và " +
                    (name.contains("MOCHA") ? "sô-cô-la thơm ngọt" : "caramel ngọt ngào") +
                    ", phủ lên trên là lớp bọt sữa mịn màng, tạo nên thức uống hoàn hảo cho những ngày năng động.";
        }
    }

    private String generateFreezeDescription(String name) {
        if (name.contains("TRÀ XANH")) {
            return "Sự kết hợp tinh tế giữa trà xanh Nhật Bản, đá xay mịn và sữa tươi, tạo nên thức uống thanh mát với hương vị cân bằng giữa ngọt nhẹ và đắng thanh của trà.";
        } else if (name.contains("CHOCO")) {
            return "Sô-cô-la đậm đà hòa quyện cùng sữa tươi và đá xay mịn, phủ lên trên là kem tươi và sốt sô-cô-la, mang đến trải nghiệm ngọt ngào khó cưỡng.";
        } else if (name.contains("COOKIES")) {
            return "Hương vị độc đáo từ bánh cookies hòa quyện cùng kem tươi và đá xay mịn, tạo nên thức uống béo ngậy với những mẩu bánh giòn tan đầy thú vị.";
        } else if (name.contains("CLASSIC")) {
            return "Thức uống đặc trưng của BCoffee với sự kết hợp giữa cà phê đậm đà, sữa tươi và đá xay mịn, mang đến hương vị cân bằng và sảng khoái.";
        } else {
            return "Sự kết hợp hài hòa giữa cà phê thơm ngon, caramel ngọt ngào và đá xay mịn, phủ lên trên là lớp kem tươi và sốt caramel, tạo nên thức uống ngọt ngào không thể cưỡng lại.";
        }
    }

    private String generateTeaDescription(String name) {
        if (name.contains("SEN VÀNG")) {
            return "Sự kết hợp tinh tế giữa trà oolong, hoa sen thơm ngát và " +
                    (name.contains("HẠT SEN") ? "hạt sen mềm dẻo" : "củ năng giòn ngọt") +
                    ", mang đến hương vị thanh tao đặc trưng của việt nam.";
        } else if (name.contains("XANH ĐẬU ĐỎ")) {
            return "Trà xanh thanh mát hòa quyện cùng đậu đỏ dẻo thơm, tạo nên thức uống cân bằng giữa vị chát nhẹ của trà và vị ngọt tự nhiên từ đậu đỏ.";
        } else if (name.contains("THẠCH ĐÀO")) {
            return "Trà đào thơm ngát kết hợp với thạch đào dai mềm, mang đến trải nghiệm thưởng thức trọn vẹn với hương vị đào tươi mát và thơm ngọt.";
        } else if (name.contains("THANH ĐÀO")) {
            return "Sự hòa quyện tuyệt vời giữa trà đen, đào tươi và siro đào, tạo nên thức uống thanh mát với hương thơm quyến rũ và vị ngọt dịu của trái đào.";
        } else {
            return "Trà đen thơm ngát kết hợp cùng vải tươi ngọt và thạch vải dai mềm, mang đến thức uống giải khát tuyệt vời với hương vị trái cây nhiệt đới đặc trưng.";
        }
    }

    private String generateFoodDescription(String name) {
        if (name.contains("TIRAMISU")) {
            return "Bộ bánh ngọt cao cấp với hương vị tiramisu đặc trưng Ý, lớp bánh mềm mịn thấm đẫm cà phê hòa quyện cùng kem mascarpone béo ngậy, rắc bột ca cao đắng thanh.";
        } else if (name.contains("MUFFIN")) {
            return "Bộ bánh muffin thơm ngon với nhiều hương vị: socola đắng, việt quất và vani, được nướng mỗi ngày để đảm bảo độ tươi ngon và hương thơm quyến rũ.";
        } else if (name.contains("CHOCO")) {
            return "Bộ bánh ngọt socola đặc biệt với lớp vỏ giòn tan và nhân socola đen đậm đà, được trang trí thêm lớp ganache mịn màng và trái cây tươi.";
        } else if (name.contains("BÁNH MÌ TRUYỀN THỐNG")) {
            return "Bánh mì Việt Nam giòn rụm bên ngoài, mềm xốp bên trong, kết hợp nhiều loại nhân thơm ngon: thịt, pate, rau thơm và nước sốt đặc biệt của BCoffee.";
        } else if (name.contains("PATE")) {
            return "Bánh mì que giòn tan kết hợp với pate thơm béo được chế biến theo công thức đặc biệt của BCoffee, mang đến hương vị đậm đà khó quên.";
        } else if (name.contains("GÀ")) {
            return "Bánh mì que giòn rụm với nhân gà xé kết hợp cùng phô mai béo ngậy và rau thơm tươi, tạo nên món ăn nhẹ thơm ngon và đầy dinh dưỡng.";
        } else if (name.contains("BÒ")) {
            return "Bánh mì que thơm ngon với thịt bò xé chín mềm kết hợp cùng phô mai tan chảy và sốt đặc biệt, mang đến hương vị đậm đà và phong phú.";
        } else {
            return "Croissant truyền thống với lớp vỏ giòn xốp, được làm từ bơ Pháp thượng hạng tạo nên hàng trăm lớp bánh mỏng, mang đến hương vị béo ngậy đặc trưng.";
        }
    }

    private String generateOtherDescription(String name) {
        if (name.contains("200G")) {
            return "Gói cà phê truyền thống 200g với hạt cà phê Robusta và Arabica chọn lọc từ Tây Nguyên, rang xay theo công thức độc quyền của BCoffee, mang đến hương vị đậm đà, thơm ngon.";
        } else if (name.contains("1KG")) {
            return "Gói cà phê truyền thống 1kg với hạt cà phê thượng hạng, rang mộc để giữ trọn hương thơm và dư vị đậm đà đặc trưng của vùng Tây Nguyên, là lựa chọn lý tưởng cho những người yêu cà phê.";
        } else if (name.contains("ĐEN")) {
            return "Lốc 6 lon cà phê đen 185ml tiện lợi, giữ nguyên hương vị đậm đà của cà phê pha phin truyền thống, là lựa chọn hoàn hảo để thưởng thức bất cứ lúc nào.";
        } else {
            return "Lốc 6 lon cà phê sữa 185ml thơm ngon, với tỷ lệ cà phê và sữa đặc hoàn hảo, mang đến hương vị đậm đà nhưng không quá ngọt, tiện lợi để thưởng thức mọi lúc mọi nơi.";
        }
    }
}
