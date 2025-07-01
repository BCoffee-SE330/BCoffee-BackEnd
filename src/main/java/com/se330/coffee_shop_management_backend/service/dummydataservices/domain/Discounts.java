package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.entity.Branch;
import com.se330.coffee_shop_management_backend.entity.Discount;
import com.se330.coffee_shop_management_backend.entity.product.Product;
import com.se330.coffee_shop_management_backend.entity.product.ProductVariant;
import com.se330.coffee_shop_management_backend.repository.BranchRepository;
import com.se330.coffee_shop_management_backend.repository.DiscountRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductRepository;
import com.se330.coffee_shop_management_backend.repository.productrepositories.ProductVariantRepository;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class Discounts {
    private final DiscountRepository discountRepository;
    private final BranchRepository branchRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void create() {
        log.info("Creating discount dummy data...");
        List<Discount> discounts = createDiscount();
        createDiscountProductVariant(discounts);
        log.info("Created {} discounts", discounts.size());
    }

    public List<Discount> createDiscount() {
        List<Branch> branches = branchRepository.findAll();
        if (branches.isEmpty()) {
            log.error("Cannot create discounts: No branches found");
            return Collections.emptyList();
        }

        Random random = new Random();
        List<Discount> discounts = new ArrayList<>();

        // Mã giảm giá theo phần trăm
        List<Map<String, Object>> percentageDiscounts = new ArrayList<>();
        percentageDiscounts.add(Map.of(
                "name", "Mừng Ngày Lễ Lớn",
                "desc", "Giảm 20% cho tất cả đơn hàng nhân dịp lễ lớn, áp dụng cho khách hàng thân thiết.",
                "code", "LENHAY20",
                "value", new BigDecimal("20"),
                "minValue", new BigDecimal("100000")
        ));
        percentageDiscounts.add(Map.of(
                "name", "Khuyến Mãi Tết Nguyên Đán",
                "desc", "Mừng xuân mới với ưu đãi giảm 15% cho tất cả sản phẩm, chúc quý khách năm mới an khang thịnh vượng.",
                "code", "TET2023",
                "value", new BigDecimal("15"),
                "minValue", new BigDecimal("50000")
        ));
        percentageDiscounts.add(Map.of(
                "name", "Sinh Nhật BCoffee",
                "desc", "Mừng sinh nhật 5 năm BCoffee với ưu đãi giảm 25% cho tất cả sản phẩm. Cảm ơn quý khách đã đồng hành cùng chúng tôi.",
                "code", "SINHNHAT5",
                "value", new BigDecimal("25"),
                "minValue", new BigDecimal("150000")
        ));
        percentageDiscounts.add(Map.of(
                "name", "Quốc Tế Phụ Nữ",
                "desc", "Giảm 10% nhân ngày Quốc tế Phụ nữ 8/3, dành tặng cho một nửa xinh đẹp của thế giới.",
                "code", "WOMEN10",
                "value", new BigDecimal("10"),
                "minValue", new BigDecimal("0")
        ));
        percentageDiscounts.add(Map.of(
                "name", "Chào Mừng Thành Viên Mới",
                "desc", "Giảm 30% cho đơn hàng đầu tiên của thành viên mới. Chào mừng bạn đến với gia đình BCoffee!",
                "code", "WELCOME30",
                "value", new BigDecimal("30"),
                "minValue", new BigDecimal("50000")
        ));
        percentageDiscounts.add(Map.of("name", "Xuân Rực Rỡ", "desc", "Đón xuân mới với ưu đãi 25% toàn bộ menu", "code", "XUAN25", "value", new BigDecimal("25"), "minValue", new BigDecimal("120000")));
        percentageDiscounts.add(Map.of("name", "Hè Sôi Động", "desc", "Giảm 15% đồ uống mát lạnh cho ngày hè oi ả", "code", "HE15", "value", new BigDecimal("15"), "minValue", new BigDecimal("80000")));
        percentageDiscounts.add(Map.of("name", "Thu Vàng Ưu Đãi", "desc", "Giảm 20% khi đặt đồ uống nóng mùa thu", "code", "THU20", "value", new BigDecimal("20"), "minValue", new BigDecimal("100000")));
        percentageDiscounts.add(Map.of("name", "Đông Ấm Áp", "desc", "Giảm 30% cho các loại trà nóng và cà phê phin", "code", "DONG30", "value", new BigDecimal("30"), "minValue", new BigDecimal("150000")));
        percentageDiscounts.add(Map.of("name", "Mưa Giảm Giá", "desc", "Giảm 10% tất cả đơn hàng vào ngày mưa", "code", "MUA10", "value", new BigDecimal("10"), "minValue", new BigDecimal("50000")));
        percentageDiscounts.add(Map.of("name", "World Coffee Day", "desc", "Giảm 40% nhân ngày Cà phê Thế giới 1/10", "code", "COFFEE40", "value", new BigDecimal("40"), "minValue", new BigDecimal("200000")));
        percentageDiscounts.add(Map.of("name", "Black Friday", "desc", "Sale đen - Giảm 50% cho đơn hàng từ 300K", "code", "BLACK50", "value", new BigDecimal("50"), "minValue", new BigDecimal("300000")));
        percentageDiscounts.add(Map.of("name", "Cyber Monday", "desc", "Giảm 35% khi đặt hàng qua app", "code", "CYBER35", "value", new BigDecimal("35"), "minValue", new BigDecimal("100000")));
        percentageDiscounts.add(Map.of("name", "Ngày Gia Đình", "desc", "Giảm 20% cho combo gia đình 4 người", "code", "GIA_DINH20", "value", new BigDecimal("20"), "minValue", new BigDecimal("250000")));
        percentageDiscounts.add(Map.of("name", "Halloween", "desc", "Giảm 15% cho đồ uống màu đen/cam", "code", "HALLOWEEN15", "value", new BigDecimal("15"), "minValue", new BigDecimal("70000")));
        percentageDiscounts.add(Map.of("name", "Trà Đào Mùa Hè", "desc", "Giảm 25% tất cả các loại trà đào", "code", "TRA_DAO25", "value", new BigDecimal("25"), "minValue", new BigDecimal("90000")));
        percentageDiscounts.add(Map.of("name", "Cà Phê Mùa Đông", "desc", "Giảm 20% cà phê phin sữa đá/nóng", "code", "CFDONG20", "value", new BigDecimal("20"), "minValue", new BigDecimal("80000")));
        percentageDiscounts.add(Map.of("name", "Matcha Mùa Xuân", "desc", "Giảm 15% đồ uống matcha nhân mùa hoa anh đào", "code", "MATCHA15", "value", new BigDecimal("15"), "minValue", new BigDecimal("60000")));
        percentageDiscounts.add(Map.of("name", "Sinh Tố Mùa Hè", "desc", "Giảm 30% các loại sinh tố trái cây", "code", "SINHTO30", "value", new BigDecimal("30"), "minValue", new BigDecimal("120000")));
        percentageDiscounts.add(Map.of("name", "Lẩu Mùa Đông", "desc", "Giảm 40% set lẩu cho 2 người", "code", "LAU40", "value", new BigDecimal("40"), "minValue", new BigDecimal("350000")));
        percentageDiscounts.add(Map.of("name", "Thành Viên Vàng", "desc", "Giảm 15% cho khách hàng thẻ vàng", "code", "GOLD15", "value", new BigDecimal("15"), "minValue", new BigDecimal("0")));
        percentageDiscounts.add(Map.of("name", "Thành Viên Bạch Kim", "desc", "Giảm 25% cho khách hàng thẻ bạch kim", "code", "PLATINUM25", "value", new BigDecimal("25"), "minValue", new BigDecimal("0")));
        percentageDiscounts.add(Map.of("name", "Đặt Hàng App", "desc", "Giảm 10% khi đặt hàng qua ứng dụng", "code", "APP10", "value", new BigDecimal("10"), "minValue", new BigDecimal("50000")));
        percentageDiscounts.add(Map.of("name", "Giới Thiệu Bạn", "desc", "Giảm 20% cho cả bạn và người được giới thiệu", "code", "REFER20", "value", new BigDecimal("20"), "minValue", new BigDecimal("100000")));
        percentageDiscounts.add(Map.of("name", "Sinh Nhật VIP", "desc", "Giảm 50% đơn hàng sinh nhật cho VIP", "code", "VIP_BDAY50", "value", new BigDecimal("50"), "minValue", new BigDecimal("200000")));
        percentageDiscounts.add(Map.of("name", "Giờ Vàng", "desc", "Giảm 30% từ 14h-16h các ngày trong tuần", "code", "GOLDEN30", "value", new BigDecimal("30"), "minValue", new BigDecimal("100000")));
        percentageDiscounts.add(Map.of("name", "Ngày Môi Trường", "desc", "Giảm 20% khi mang theo ly cá nhân", "code", "GREEN20", "value", new BigDecimal("20"), "minValue", new BigDecimal("0")));
        percentageDiscounts.add(Map.of("name", "Học Sinh/Sinh Viên", "desc", "Giảm 15% khi xuất trình thẻ học sinh", "code", "STUDENT15", "value", new BigDecimal("15"), "minValue", new BigDecimal("50000")));
        percentageDiscounts.add(Map.of("name", "Ngày Lương", "desc", "Giảm 10% vào ngày 15 hàng tháng", "code", "PAYDAY10", "value", new BigDecimal("10"), "minValue", new BigDecimal("80000")));
        percentageDiscounts.add(Map.of("name", "Review Quán", "desc", "Giảm 25% khi check-in và review trên Facebook", "code", "REVIEW25", "value", new BigDecimal("25"), "minValue", new BigDecimal("120000")));
        percentageDiscounts.add(Map.of("name", "Cold Brew Thả Ga", "desc", "Giảm 35% tất cả cold brew tháng 7", "code", "COLD35", "value", new BigDecimal("35"), "minValue", new BigDecimal("90000")));
        percentageDiscounts.add(Map.of("name", "Trà Sen Vàng", "desc", "Giảm 20% trà sen nhập khẩu từ Huế", "code", "SEN20", "value", new BigDecimal("20"), "minValue", new BigDecimal("110000")));
        percentageDiscounts.add(Map.of("name", "Cà Phê Hạt Mới", "desc", "Giảm 15% blend cà phê mùa vụ mới", "code", "NEWBEAN15", "value", new BigDecimal("15"), "minValue", new BigDecimal("80000")));
        percentageDiscounts.add(Map.of("name", "Mua 1 Tặng 1", "desc", "Giảm 50% khi mua 2 món giống nhau", "code", "BUY1GET1", "value", new BigDecimal("50"), "minValue", new BigDecimal("120000")));
        percentageDiscounts.add(Map.of("name", "Combo Nhóm", "desc", "Giảm 30% khi mua 4 đồ uống bất kỳ", "code", "TEAM30", "value", new BigDecimal("30"), "minValue", new BigDecimal("200000")));
        percentageDiscounts.add(Map.of("name", "VinID Ưu Đãi", "desc", "Giảm 25% khi thanh toán qua VinID", "code", "VINID25", "value", new BigDecimal("25"), "minValue", new BigDecimal("100000")));
        percentageDiscounts.add(Map.of("name", "Momo Monday", "desc", "Giảm 15% khi thanh toán qua Momo thứ 2", "code", "MOMO15", "value", new BigDecimal("15"), "minValue", new BigDecimal("50000")));
        percentageDiscounts.add(Map.of("name", "World Cup 2026", "desc", "Giảm 20% khi mặc áo đội bóng đến quán", "code", "WORLDCUP20", "value", new BigDecimal("20"), "minValue", new BigDecimal("0")));
        percentageDiscounts.add(Map.of("name", "Champion League", "desc", "Giảm 10% cho mỗi bàn thắng đội nhà", "code", "UCL10", "value", new BigDecimal("10"), "minValue", new BigDecimal("70000")));
        percentageDiscounts.add(Map.of("name", "Ngược Đời", "desc", "Giảm 5% mỗi năm tuổi (tối đa 30%)", "code", "AGE5", "value", new BigDecimal("30"), "minValue", new BigDecimal("150000")));
        percentageDiscounts.add(Map.of("name", "Mưa Càng To Ưu Đãi Càng Lớn", "desc", "Giảm 1% cho mỗi mm lượng mưa", "code", "RAIN1", "value", new BigDecimal("30"), "minValue", new BigDecimal("100000")));
        percentageDiscounts.add(Map.of("name", "Ngày Nhà Giáo", "desc", "Giảm 20% cho giáo viên 20/11", "code", "TEACHER20", "value", new BigDecimal("20"), "minValue", new BigDecimal("0")));
        percentageDiscounts.add(Map.of("name", "Quốc Khánh", "desc", "Giảm 10% toàn bộ menu ngày 2/9", "code", "NATIONAL10", "value", new BigDecimal("10"), "minValue", new BigDecimal("50000")));
        percentageDiscounts.add(Map.of("name", "Tân Binh", "desc", "Giảm 40% cho 3 đơn hàng đầu tiên", "code", "ROOKIE40", "value", new BigDecimal("40"), "minValue", new BigDecimal("60000")));
        percentageDiscounts.add(Map.of("name", "Lão Luyện", "desc", "Giảm 15% cho khách có trên 50 đơn", "code", "VETERAN15", "value", new BigDecimal("15"), "minValue", new BigDecimal("0")));
        percentageDiscounts.add(Map.of("name", "Quận 7 Ưu Đãi", "desc", "Giảm 10% cho cư dân Quận 7", "code", "Q710", "value", new BigDecimal("10"), "minValue", new BigDecimal("80000")));
        percentageDiscounts.add(Map.of("name", "Nội Thành Free", "desc", "Giảm 5% + freeship cho nội thành HCM", "code", "INNER5", "value", new BigDecimal("5"), "minValue", new BigDecimal("100000")));
        percentageDiscounts.add(Map.of("name", "Sáng Sớm", "desc", "Giảm 15% cho đơn hàng trước 8h sáng", "code", "EARLY15", "value", new BigDecimal("15"), "minValue", new BigDecimal("50000")));
        percentageDiscounts.add(Map.of("name", "Đêm Muộn", "desc", "Giảm 20% cho đơn hàng sau 22h", "code", "LATE20", "value", new BigDecimal("20"), "minValue", new BigDecimal("90000")));
        percentageDiscounts.add(Map.of("name", "Tử Tế", "desc", "Giảm 10% khi kể 1 câu chuyện tốt", "code", "KIND10", "value", new BigDecimal("10"), "minValue", new BigDecimal("0")));
        percentageDiscounts.add(Map.of("name", "Xanh", "desc", "Giảm 7% khi đi xe đạp/xe máy điện", "code", "GREEN7", "value", new BigDecimal("7"), "minValue", new BigDecimal("70000")));

        // Mã giảm giá cố định
        List<Map<String, Object>> fixedDiscounts = new ArrayList<>();
        fixedDiscounts.add(Map.of(
                "name", "Giảm 20K Đơn Hàng",
                "desc", "Giảm ngay 20.000đ cho đơn hàng từ 100.000đ, áp dụng cho tất cả các cửa hàng.",
                "code", "GIAM20K",
                "value", new BigDecimal("20000"),
                "minValue", new BigDecimal("100000")
        ));
        fixedDiscounts.add(Map.of(
                "name", "Ưu Đãi 50K Cuối Tuần",
                "desc", "Giảm ngay 50.000đ cho đơn hàng từ 200.000đ vào cuối tuần. Thưởng thức cà phê ngon với giá tốt hơn!",
                "code", "WEEKEND50",
                "value", new BigDecimal("50000"),
                "minValue", new BigDecimal("200000")
        ));
        fixedDiscounts.add(Map.of(
                "name", "Combo Tiết Kiệm",
                "desc", "Giảm ngay 30.000đ khi đặt combo 2 đồ uống và 1 bánh, áp dụng cho tất cả chi nhánh.",
                "code", "COMBO30K",
                "value", new BigDecimal("30000"),
                "minValue", new BigDecimal("150000")
        ));
        fixedDiscounts.add(Map.of(
                "name", "Freeship Nội Thành",
                "desc", "Miễn phí giao hàng trị giá 15.000đ cho đơn hàng từ 100.000đ trong phạm vi 5km.",
                "code", "FREESHIP",
                "value", new BigDecimal("15000"),
                "minValue", new BigDecimal("100000")
        ));
        fixedDiscounts.add(Map.of(
                "name", "Quà Tặng Sinh Nhật",
                "desc", "Tặng ngay 100.000đ cho đơn hàng nhân dịp sinh nhật khách hàng, áp dụng cho tất cả sản phẩm.",
                "code", "HPBD100K",
                "value", new BigDecimal("100000"),
                "minValue", new BigDecimal("300000")
        ));
        fixedDiscounts.add(Map.of("name", "Free Trà Đào", "desc", "Giảm 45K - tương đương 1 ly trà đào size M", "code", "FREE_TRA_DAO", "value", new BigDecimal("45000"), "minValue", new BigDecimal("150000")));
        fixedDiscounts.add(Map.of("name", "Free Cà Phê", "desc", "Giảm 35K - tương đương 1 cà phê đen đá", "code", "FREE_CF", "value", new BigDecimal("35000"), "minValue", new BigDecimal("120000")));
        fixedDiscounts.add(Map.of("name", "Free Matcha", "desc", "Giảm 55K - tương đương 1 matcha latte size M", "code", "FREE_MATCHA", "value", new BigDecimal("55000"), "minValue", new BigDecimal("200000")));
        fixedDiscounts.add(Map.of("name", "Free Sinh Tố", "desc", "Giảm 60K - tương đương 1 sinh tố bơ", "code", "FREE_SINHTO", "value", new BigDecimal("60000"), "minValue", new BigDecimal("250000")));
        fixedDiscounts.add(Map.of("name", "Free Trà Sữa", "desc", "Giảm 50K - tương đương 1 trà sữa trân châu", "code", "FREE_TRASUA", "value", new BigDecimal("50000"), "minValue", new BigDecimal("180000")));
        fixedDiscounts.add(Map.of("name", "Combo Sáng", "desc", "Giảm 40K khi mua combo bánh mì + cà phê", "code", "COMBO_SANG", "value", new BigDecimal("40000"), "minValue", new BigDecimal("120000")));
        fixedDiscounts.add(Map.of("name", "Combo Trưa", "desc", "Giảm 70K khi mua combo mì Ý + nước ép", "code", "COMBO_TRUA", "value", new BigDecimal("70000"), "minValue", new BigDecimal("220000")));
        fixedDiscounts.add(Map.of("name", "Combo Chiều", "desc", "Giảm 50K khi mua combo bánh ngọt + trà", "code", "COMBO_CHIEU", "value", new BigDecimal("50000"), "minValue", new BigDecimal("150000")));
        fixedDiscounts.add(Map.of("name", "Combo Tối", "desc", "Giảm 80K khi mua combo pizza + 2 đồ uống", "code", "COMBO_TOI", "value", new BigDecimal("80000"), "minValue", new BigDecimal("300000")));
        fixedDiscounts.add(Map.of("name", "Combo Gia Đình", "desc", "Giảm 120K cho đơn hàng từ 500K", "code", "COMBO_FAMILY", "value", new BigDecimal("120000"), "minValue", new BigDecimal("500000")));
        fixedDiscounts.add(Map.of("name", "Freeship Xtra", "desc", "Miễn phí ship đến 30K cho đơn từ 150K", "code", "FREESHIP_XTRA", "value", new BigDecimal("30000"), "minValue", new BigDecimal("150000")));
        fixedDiscounts.add(Map.of("name", "Siêu Freeship", "desc", "Miễn phí ship đến 50K cho đơn từ 300K", "code", "SUPER_FREESHIP", "value", new BigDecimal("50000"), "minValue", new BigDecimal("300000")));
        fixedDiscounts.add(Map.of("name", "Ship Nửa Giá", "desc", "Giảm 50% phí ship tối đa 25K", "code", "HALF_SHIP", "value", new BigDecimal("25000"), "minValue", new BigDecimal("100000")));
        fixedDiscounts.add(Map.of("name", "Ship 0Đ Quận 1", "desc", "Miễn phí ship trong Quận 1", "code", "Q1_FREESHIP", "value", new BigDecimal("20000"), "minValue", new BigDecimal("100000")));
        fixedDiscounts.add(Map.of("name", "Ship Đồng Giá 10K", "desc", "Chỉ 10K phí ship cho mọi đơn hàng", "code", "SHIP10K", "value", new BigDecimal("15000"), "minValue", new BigDecimal("50000")));
        fixedDiscounts.add(Map.of("name", "Voucher 100K", "desc", "Giảm thẳng 100K cho đơn từ 400K", "code", "VOUCHER100", "value", new BigDecimal("100000"), "minValue", new BigDecimal("400000")));
        fixedDiscounts.add(Map.of("name", "Hoàn Tiền 20%", "desc", "Hoàn 20% giá trị đơn (tối đa 50K) vào ví điện tử", "code", "REFUND20", "value", new BigDecimal("50000"), "minValue", new BigDecimal("250000")));
        fixedDiscounts.add(Map.of("name", "Đổi Điểm", "desc", "Giảm 30K khi sử dụng 100 điểm tích lũy", "code", "POINT30", "value", new BigDecimal("30000"), "minValue", new BigDecimal("100000")));
        fixedDiscounts.add(Map.of("name", "Mừng Khai Trương", "desc", "Giảm 50K cho 100 khách hàng đầu tiên", "code", "KHAITRUONG50", "value", new BigDecimal("50000"), "minValue", new BigDecimal("150000")));
        fixedDiscounts.add(Map.of("name", "Ngày Đặc Biệt", "desc", "Giảm 99K cho đơn từ 300K vào ngày 9/9", "code", "SPECIAL99", "value", new BigDecimal("99000"), "minValue", new BigDecimal("300000")));
        fixedDiscounts.add(Map.of("name", "Giảm Giá Số Đẹp", "desc", "Giảm 88K cho đơn 288K", "code", "LUCKY88", "value", new BigDecimal("88000"), "minValue", new BigDecimal("288000")));
        fixedDiscounts.add(Map.of("name", "Thứ 6 Ngày 13", "desc", "Giảm 66K vào thứ 6 ngày 13", "code", "FRIDAY13", "value", new BigDecimal("66000"), "minValue", new BigDecimal("166000")));
        fixedDiscounts.add(Map.of("name", "Giờ Vàng", "desc", "Giảm 25K từ 10h-12h hàng ngày", "code", "GOLD_HOUR", "value", new BigDecimal("25000"), "minValue", new BigDecimal("100000")));
        fixedDiscounts.add(Map.of("name", "Mưa Giảm Giá", "desc", "Giảm 20K vào ngày mưa", "code", "RAIN20", "value", new BigDecimal("20000"), "minValue", new BigDecimal("80000")));
        fixedDiscounts.add(Map.of("name", "Hot Deal", "desc", "Giảm 75K cho đơn từ 250K - áp dụng 10 mã đầu tiên mỗi ngày", "code", "HOT75", "value", new BigDecimal("75000"), "minValue", new BigDecimal("250000")));
        fixedDiscounts.add(Map.of("name", "Free Topping", "desc", "Miễn phí 1 topping bất kỳ trị giá 15K", "code", "FREE_TOPPING", "value", new BigDecimal("15000"), "minValue", new BigDecimal("100000")));
        fixedDiscounts.add(Map.of("name", "Giảm Size L", "desc", "Giảm 25K khi nâng size M lên L", "code", "UP_SIZE", "value", new BigDecimal("25000"), "minValue", new BigDecimal("120000")));
        fixedDiscounts.add(Map.of("name", "Combo Pet", "desc", "Giảm 40K khi mua đồ uống + bánh cho thú cưng", "code", "PET40", "value", new BigDecimal("40000"), "minValue", new BigDecimal("150000")));
        fixedDiscounts.add(Map.of("name", "Combo Sách", "desc", "Giảm 30K khi mua đồ uống + tạp chí", "code", "BOOK30", "value", new BigDecimal("30000"), "minValue", new BigDecimal("130000")));
        fixedDiscounts.add(Map.of("name", "Vietcombank", "desc", "Giảm 50K khi thanh toán qua Vietcombank", "code", "VCB50", "value", new BigDecimal("50000"), "minValue", new BigDecimal("200000")));
        fixedDiscounts.add(Map.of("name", "ZaloPay", "desc", "Giảm 35K khi dùng ZaloPay thứ 4", "code", "ZALO35", "value", new BigDecimal("35000"), "minValue", new BigDecimal("150000")));
        fixedDiscounts.add(Map.of("name", "Cưới", "desc", "Giảm 99K cho đơn đặt tiệc cưới", "code", "WEDDING99", "value", new BigDecimal("99000"), "minValue", new BigDecimal("500000")));
        fixedDiscounts.add(Map.of("name", "Sinh Nhật Bất Ngờ", "desc", "Tặng ngẫu nhiên 20-100K cho đơn sinh nhật", "code", "SURPRISE", "value", new BigDecimal("100000"), "minValue", new BigDecimal("200000")));
        fixedDiscounts.add(Map.of("name", "Món Mới", "desc", "Giảm 45K khi thử món mới trong menu", "code", "NEW45", "value", new BigDecimal("45000"), "minValue", new BigDecimal("160000")));
        fixedDiscounts.add(Map.of("name", "Phản Hồi", "desc", "Giảm 30K khi gửi feedback về món", "code", "FEEDBACK30", "value", new BigDecimal("30000"), "minValue", new BigDecimal("120000")));
        fixedDiscounts.add(Map.of("name", "Thứ 7 Vui", "desc", "Giảm 60K cho đơn từ 250K thứ 7", "code", "SAT60", "value", new BigDecimal("60000"), "minValue", new BigDecimal("250000")));
        fixedDiscounts.add(Map.of("name", "Chủ Nhật", "desc", "Giảm 40K cho đơn chủ nhật", "code", "SUN40", "value", new BigDecimal("40000"), "minValue", new BigDecimal("180000")));
        fixedDiscounts.add(Map.of("name", "Đổi 100 Điểm", "desc", "Giảm 50K khi sử dụng 100 điểm", "code", "POINT50", "value", new BigDecimal("50000"), "minValue", new BigDecimal("150000")));
        fixedDiscounts.add(Map.of("name", "Vé Xem Phim", "desc", "Tặng vé CGV trị giá 60K khi mua đủ 300K", "code", "MOVIE60", "value", new BigDecimal("60000"), "minValue", new BigDecimal("300000")));
        fixedDiscounts.add(Map.of("name", "Cuối Tháng", "desc", "Giảm 70K cho đơn từ 300K 3 ngày cuối tháng", "code", "MONTH_END70", "value", new BigDecimal("70000"), "minValue", new BigDecimal("300000")));
        fixedDiscounts.add(Map.of("name", "Đầu Tháng", "desc", "Giảm 40K cho đơn đầu tháng", "code", "MONTH_START40", "value", new BigDecimal("40000"), "minValue", new BigDecimal("200000")));
        fixedDiscounts.add(Map.of("name", "Nắng Nóng", "desc", "Giảm 25K các đồ uống lạnh khi nhiệt độ >35°C", "code", "HOT25", "value", new BigDecimal("25000"), "minValue", new BigDecimal("100000")));
        fixedDiscounts.add(Map.of("name", "Mưa Giông", "desc", "Giảm 30K khi đặt đồ uống nóng ngày mưa", "code", "STORM30", "value", new BigDecimal("30000"), "minValue", new BigDecimal("120000")));
        fixedDiscounts.add(Map.of("name", "Mã Số Đẹp", "desc", "Giảm 77K cho đơn 277K", "code", "LUCKY77", "value", new BigDecimal("77000"), "minValue", new BigDecimal("277000")));
        fixedDiscounts.add(Map.of("name", "Ngẫu Nhiên", "desc", "Giảm 10-50K ngẫu nhiên mỗi đơn", "code", "RANDOM", "value", new BigDecimal("50000"), "minValue", new BigDecimal("100000")));


        LocalDateTime now = LocalDateTime.now();

        // Tạo discount phần trăm
        for (Map<String, Object> info : percentageDiscounts) {
            Branch branch = branches.get(random.nextInt(branches.size()));

            // Tạo discount với phân phối: 60% đã hết hạn, 35% đang có hiệu lực, 5% sẽ có hiệu lực
            int randomValue = random.nextInt(100); // Random value from 0-99
            LocalDateTime startDate, endDate;
            boolean isActive;

            if (randomValue < 60) { // 60% chance - đã hết hạn
                // Since branches have been open for 10 years, we can use a wider range for expired discounts
                int monthsAgo = 1 + random.nextInt(119); // Random between 1 and 119 months ago (up to nearly 10 years)
                startDate = now.minusMonths(monthsAgo + 1 + random.nextInt(3)); // Start date is even earlier
                endDate = now.minusMonths(monthsAgo);
                isActive = false;
            } else if (randomValue < 95) { // 35% chance - đang có hiệu lực
                startDate = now.minusMonths(random.nextInt(6)); // Started up to 6 months ago
                endDate = now.plusWeeks(1 + random.nextInt(12)); // Will end in 1-12 weeks
                isActive = true;
            } else { // 5% chance - sẽ có hiệu lực
                int daysToStart = 1 + random.nextInt(30); // Will start in 1-30 days
                startDate = now.plusDays(daysToStart);
                endDate = startDate.plusMonths(1 + random.nextInt(3)); // Will last 1-3 months
                isActive = true;
            }

            Discount discount = Discount.builder()
                    .discountName((String) info.get("name"))
                    .discountDescription((String) info.get("desc"))
                    .discountType(Constants.DiscountTypeEnum.PERCENTAGE)
                    .discountValue((BigDecimal) info.get("value"))
                    .discountCode((String) info.get("code"))
                    .discountStartDate(startDate)
                    .discountEndDate(endDate)
                    .discountMaxUsers(100 + random.nextInt(900))
                    .discountUserCount(random.nextInt(50))
                    .discountMaxPerUser(1 + random.nextInt(3))
                    .discountMinOrderValue((BigDecimal) info.get("minValue"))
                    .discountIsActive(isActive)
                    .branch(branch)
                    .build();

            discounts.add(discount);
        }

        // Tạo discount cố định
        for (Map<String, Object> info : fixedDiscounts) {
            Branch branch = branches.get(random.nextInt(branches.size()));

            // Tạo discount với phân phối: 60% đã hết hạn, 35% đang có hiệu lực, 5% sẽ có hiệu lực
            int randomValue = random.nextInt(100); // Random value from 0-99
            LocalDateTime startDate, endDate;
            boolean isActive;

            if (randomValue < 60) { // 60% chance - đã hết hạn
                // Since branches have been open for 10 years, we can use a wider range for expired discounts
                int monthsAgo = 1 + random.nextInt(119); // Random between 1 and 119 months ago (up to nearly 10 years)
                startDate = now.minusMonths(monthsAgo + 1 + random.nextInt(3)); // Start date is even earlier
                endDate = now.minusMonths(monthsAgo);
                isActive = false;
            } else if (randomValue < 95) { // 35% chance - đang có hiệu lực
                startDate = now.minusMonths(random.nextInt(6)); // Started up to 6 months ago
                endDate = now.plusWeeks(1 + random.nextInt(12)); // Will end in 1-12 weeks
                isActive = true;
            } else { // 5% chance - sẽ có hiệu lực
                int daysToStart = 1 + random.nextInt(30); // Will start in 1-30 days
                startDate = now.plusDays(daysToStart);
                endDate = startDate.plusMonths(1 + random.nextInt(3)); // Will last 1-3 months
                isActive = true;
            }

            Discount discount = Discount.builder()
                    .discountName((String) info.get("name"))
                    .discountDescription((String) info.get("desc"))
                    .discountType(Constants.DiscountTypeEnum.AMOUNT)
                    .discountValue((BigDecimal) info.get("value"))
                    .discountCode((String) info.get("code"))
                    .discountStartDate(startDate)
                    .discountEndDate(endDate)
                    .discountMaxUsers(100 + random.nextInt(900))
                    .discountUserCount(random.nextInt(50))
                    .discountMaxPerUser(1 + random.nextInt(3))
                    .discountMinOrderValue((BigDecimal) info.get("minValue"))
                    .discountIsActive(isActive)
                    .branch(branch)
                    .build();

            discounts.add(discount);
        }

        return discountRepository.saveAll(discounts);
    }

    public void createDiscountProductVariant(List<Discount> discounts) {
        if (discounts.isEmpty()) {
            log.error("No discounts to associate with product variants");
            return;
        }

        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            log.error("No products found to associate with discounts");
            return;
        }

        // Lấy tất cả product variants
        List<ProductVariant> allVariants = productVariantRepository.findAll();
        if (allVariants.isEmpty()) {
            log.error("No product variants found to associate with discounts");
            return;
        }

        Random random = new Random();

        // Với mỗi discount, gắn 1-5 products
        for (Discount discount : discounts) {
            // Chọn ngẫu nhiên 1-5 products
            int productCount = 1 + random.nextInt(5);
            Set<Product> selectedProducts = new HashSet<>();

            while (selectedProducts.size() < productCount && selectedProducts.size() < products.size()) {
                Product product = products.get(random.nextInt(products.size()));
                selectedProducts.add(product);
            }

            // Lấy tất cả product variants của các products đã chọn
            List<ProductVariant> selectedVariants = new ArrayList<>();
            for (Product product : selectedProducts) {
                for (ProductVariant variant : allVariants) {
                    if (variant.getProduct() != null && variant.getProduct().getId().equals(product.getId())) {
                        selectedVariants.add(variant);
                    }
                }
            }

            if (selectedVariants.isEmpty()) {
                continue; // Bỏ qua nếu không tìm thấy variant nào
            }

            // Thêm discount vào danh sách discounts của mỗi variant đã chọn
            for (ProductVariant variant : selectedVariants) {
                if (variant.getDiscounts() == null) {
                    variant.setDiscounts(new ArrayList<>());
                }
                variant.getDiscounts().add(discount);
            }

            // Cập nhật lại danh sách product variants cho discount
            discount.setProductVariants(selectedVariants);
        }

        // Lưu tất cả các thay đổi
        productVariantRepository.saveAll(allVariants);
        discountRepository.saveAll(discounts);

        log.info("Associated discounts with product variants: {} discounts with variants", discounts.size());
    }
}