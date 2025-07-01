package com.se330.coffee_shop_management_backend.service.dummydataservices.domain;

import com.se330.coffee_shop_management_backend.dto.request.user.CreateUserRequest;
import com.se330.coffee_shop_management_backend.entity.PaymentMethods;
import com.se330.coffee_shop_management_backend.entity.Role;
import com.se330.coffee_shop_management_backend.entity.ShippingAddresses;
import com.se330.coffee_shop_management_backend.entity.User;
import com.se330.coffee_shop_management_backend.repository.PaymentMethodsRepository;
import com.se330.coffee_shop_management_backend.repository.ShippingAddressesRepository;
import com.se330.coffee_shop_management_backend.repository.UserRepository;
import com.se330.coffee_shop_management_backend.service.RoleService;
import com.se330.coffee_shop_management_backend.service.UserService;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserBasicInfo {

    private final RoleService roleService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ShippingAddressesRepository shippingAddressesRepository;
    private final PaymentMethodsRepository paymentMethodsRepository;

    @Transactional
    public void create() throws BindException {
        createRoles();
        createUsers();
        createShippingAddresses();
        createPaymentMethods();
    }

    private void createRoles() {
        List<Role> roleList = new ArrayList<>();
        roleList.add(Role.builder().name(Constants.RoleEnum.ADMIN).build());
        roleList.add(Role.builder().name(Constants.RoleEnum.CUSTOMER).build());
        roleList.add(Role.builder().name(Constants.RoleEnum.MANAGER).build());
        roleList.add(Role.builder().name(Constants.RoleEnum.EMPLOYEE).build());

        roleService.saveList(roleList);
    }

    private void createUsers() throws BindException {
        String defaultPassword = "P@sswd123.";
        String defaultAdminAvatarUrl = "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749142598/dummy_avatar_2_t1pd3u.jpg";
        String defaultCustomerAvatarUrl = "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749142597/dummy_avatar_4_wrzhjf.jpg";
        String defaultManagerAvatarUrl = "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749142596/dummy_avatar_1_no9xzs.jpg";
        String defaultEmployeeAvatarUrl = "https://res.cloudinary.com/dzpv3mfjt/image/upload/v1749142598/dummy_avatar_3_krtmjn.jpg";

        log.info("Tạo người dùng quản trị viên (1 ADMIN)...");
        User adminUser = userService.create(CreateUserRequest.builder()
                .email("admin@example.com")
                .password(defaultPassword)
                .name("Nguyễn Văn")
                .lastName("Admin")
                .role(Constants.RoleEnum.ADMIN)
                .isEmailVerified(true)
                .phoneNumber("098" + getRandomDigits())
                .gender("Nam")
                .birthDate(LocalDateTime.now().minusYears(30 + (int)(Math.random() * 10)))
                .isBlocked(false)
                .build());
        adminUser.setAvatar(defaultAdminAvatarUrl);
        userRepository.save(adminUser);

        log.info("Tạo người dùng khách hàng (10 CUSTOMER)...");
        String[] customerFirstNames = {
                // Nam
                "Văn Anh", "Đức Anh", "Mạnh Cường", "Hữu Đạt", "Quang Dũng",
                "Thế Duy", "Bảo Đại", "Minh Đạo", "Tuấn Hải", "Ngọc Hà",
                "Trung Hiếu", "Gia Huy", "Anh Khôi", "Thành Long", "Hải Nam",
                "Quốc Phong", "Hồng Quân", "Đình Sang", "Hoàng Sơn", "Minh Tâm",
                "Nhật Tân", "Anh Tuấn", "Quang Vinh", "Tiến Vũ", "Đăng Khoa",

                // Nữ
                "Thị Ánh", "Ngọc Anh", "Mỹ Duyên", "Thu Hiền", "Thanh Hà",
                "Phương Hoa", "Thúy Hồng", "Diễm Hương", "Mai Linh", "Kim Ngân",
                "Như Quỳnh", "Minh Thư", "Kiều Trang", "Khánh Vy", "Hồng Vân",
                "Thảo Ngọc", "Bảo Trâm", "Nhật Lệ", "Thanh Mai", "Hải Yến",
                "Lan Phương", "Minh Châu", "Diệu Linh", "Thu Thủy", "Tuyết Trinh",

                // Unisex
                "Bảo Châu", "Gia Hân", "Kim Khánh", "Minh Ngọc", "Nhật Minh",
                "Phương Thảo", "Quỳnh Chi", "Thanh Thảo", "Tuệ Lâm", "Xuân Mai"
        };
        String[] customerLastNames = {
                "Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ", "Võ",
                "Đặng", "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý", "Đào", "Đinh",
                "Mai", "Lâm", "Trương", "Chu", "La", "Tạ", "Hà", "Thạch", "Giáp",
                "Đoàn", "Kim", "Quách", "Vương", "Triệu", "Cao", "Lưu", "Hứa",
                "Phùng", "Tô", "Trịnh", "Tống", "Bạch", "Hồng", "Lục", "Tiêu"
        };
        for (int i = 0; i < 10; i++) {
            String gender = customerFirstNames[i].contains("Thị") ? "Female" : "Male";
            User customerUser = userService.create(CreateUserRequest.builder()
                    .email("customer" + (i + 1) + "@example.com")
                    .password(defaultPassword)
                    .name(customerFirstNames[i])
                    .lastName(customerLastNames[i])
                    .role(Constants.RoleEnum.CUSTOMER)
                    .isEmailVerified(true)
                    .phoneNumber("09" + (7 + i % 3) + getRandomDigits())
                            .gender(gender)
                            .birthDate(LocalDateTime.now().minusYears(18 + (int)(Math.random() * 42)))
                    .isBlocked(false)
                    .build());
            customerUser.setAvatar(defaultCustomerAvatarUrl);
            userRepository.save(customerUser);
        }

        log.info("Tạo người dùng quản lý (10 MANAGER)...");
        String[] managerFirstNames = {"Phan Văn", "Lý Thị", "Trương Minh", "Mai Thị", "Dương Văn"};
        String[] managerLastNames = {"Quang", "Hòa", "Tâm", "Phương", "Khoa"};

        for (int i = 0; i < 5; i++) {
            String gender = managerFirstNames[i].contains("Thị") ? "Nữ" : "Nam";
            User managerUser = userService.create(CreateUserRequest.builder()
                    .email("manager" + (i + 1) + "@example.com")
                    .password(defaultPassword)
                    .name(managerFirstNames[i])
                    .lastName(managerLastNames[i])
                    .role(Constants.RoleEnum.MANAGER)
                    .isEmailVerified(true)
                    .isBlocked(false)
                    .phoneNumber("09" + (1 + i % 3) + getRandomDigits())
                    .gender(gender)
                    .birthDate(LocalDateTime.now().minusYears(28 + (int)(Math.random() * 15)))
                    .build());
            managerUser.setAvatar(defaultManagerAvatarUrl);
            userRepository.save(managerUser);
        }

        log.info("Tạo người dùng nhân viên (30 EMPLOYEE)...");
        String[] employeeFirstNames = {
                // Nam
                "Nguyễn Văn", "Trần Đức", "Lê Hoàng", "Phạm Quốc", "Hoàng Minh",
                "Huỳnh Thanh", "Võ Ngọc", "Đặng Trường", "Bùi Gia", "Đỗ Xuân",
                "Hồ Hữu", "Ngô Bá", "Dương Vĩnh", "Lý Vũ", "Trương Sĩ",
                "Mai Thanh", "Phan Đình", "Vũ Đăng", "Tô Thế", "Đinh Tiến",
                "Trịnh Quang", "Chu Bảo", "Lương Đình", "Chung Hải", "Thái Duy",
                "Diệp Anh", "Tạ Vĩnh", "Hà Trọng", "Thạch Tuấn", "Kim Nhật",

                // Nữ
                "Nguyễn Thị", "Trần Thị", "Lê Thị", "Phạm Thị", "Hoàng Thị",
                "Huỳnh Thị", "Võ Thị", "Đặng Thị", "Bùi Thị", "Đỗ Thị",
                "Hồ Thị", "Ngô Thị", "Dương Thị", "Lý Thị", "Trương Thị",
                "Mai Thị", "Phan Thị", "Vũ Thị", "Tô Thị", "Đinh Thị",
                "Trịnh Thị", "Chu Thị", "Lương Thị", "Chung Thị", "Thái Thị",
                "Diệp Thị", "Tạ Thị", "Hà Thị", "Thạch Thị", "Kim Thị",

                // Unisex
                "Nguyễn Minh", "Trần Anh", "Lê Bảo", "Phạm Khánh", "Hoàng Gia",
                "Huỳnh Ngọc", "Võ Nhật", "Đặng Phương", "Bùi Tuệ", "Đỗ Thiên"
        };

        String[] employeeLastNames = {
                // Nam
                "Anh", "Bảo", "Cảnh", "Dũng", "Đạt", "Hiếu", "Huy", "Khải", "Long", "Mạnh",
                "Nam", "Phúc", "Quang", "Sơn", "Thái", "Trí", "Tuấn", "Vinh", "Xuân", "Ý",
                "Bình", "Chiến", "Đức", "Hào", "Kiên", "Lộc", "Nghĩa", "Phong", "Tài", "Thắng",

                // Nữ
                "Ánh", "Bích", "Châu", "Diễm", "Giang", "Hà", "Hương", "Khanh", "Lan", "My",
                "Nga", "Oanh", "Phương", "Quỳnh", "Trinh", "Uyên", "Vy", "Xuyến", "Yến", "Hạnh",
                "Cẩm", "Duyên", "Hằng", "Linh", "Mai", "Ngân", "Như", "Phượng", "Thanh", "Thảo",

                // Unisex
                "An", "Bình", "Chi", "Duy", "Gia", "Hân", "Khuê", "Lâm", "Minh", "Ngọc",
                "Nhật", "Phú", "Quân", "Tâm", "Thư", "Vũ", "Thành", "Hải", "Nguyên", "Tú"
        };

        for (int i = 0; i < 30; i++) {
            String gender = employeeFirstNames[i].contains("Thị") ? "Nữ" : "Nam";
            User employeeUser = userService.create(CreateUserRequest.builder()
                    .email("employee" + (i + 1) + "@example.com")
                    .password(defaultPassword)
                    .name(employeeFirstNames[i])
                    .lastName(employeeLastNames[i])
                    .role(Constants.RoleEnum.EMPLOYEE)
                    .isEmailVerified(true)
                    .isBlocked(false)
                    .phoneNumber("09" + (3 + i % 6) + getRandomDigits())
                    .gender(gender)
                    .birthDate(LocalDateTime.now().minusYears(20 + (int)(Math.random() * 25)))
                    .build());
            employeeUser.setAvatar(defaultEmployeeAvatarUrl);
            userRepository.save(employeeUser);
        }
    }

    private void createShippingAddresses() {
        log.info("Creating shipping addresses...");

        // Get only CUSTOMER users
        List<User> customerUsers = userRepository.findAllByRoleName(Constants.RoleEnum.CUSTOMER);

        if (customerUsers.isEmpty()) {
            log.error("Cannot create shipping addresses: No CUSTOMER users found");
            return;
        }

        List<ShippingAddresses> addresses = new ArrayList<>();

        // Vietnamese cities and districts
        String[][] cityDistricts = {
                {"Hà Nội", "Hoàn Kiếm", "Ba Đình", "Đống Đa", "Cầu Giấy", "Hai Bà Trưng", "Hoàng Mai"},
                {"Hồ Chí Minh", "Quận 1", "Quận 3", "Quận 5", "Quận 7", "Phú Nhuận", "Bình Thạnh", "Thủ Đức"},
                {"Đà Nẵng", "Hải Châu", "Thanh Khê", "Sơn Trà", "Ngũ Hành Sơn", "Liên Chiểu"},
                {"Hải Phòng", "Hồng Bàng", "Ngô Quyền", "Lê Chân", "Kiến An", "Đồ Sơn"},
                {"Cần Thơ", "Ninh Kiều", "Bình Thủy", "Cái Răng", "Ô Môn", "Thốt Nốt"}
        };

        // Vietnamese street names
        String[] streetNames = {
                "Nguyễn Huệ", "Lê Lợi", "Trần Hưng Đạo", "Nguyễn Thị Minh Khai",
                "Võ Văn Kiệt", "Phan Xích Long", "Lý Thường Kiệt", "Điện Biên Phủ",
                "Nguyễn Đình Chiểu", "Hoàng Văn Thụ", "Nguyễn Công Trứ", "Lê Thánh Tôn",
                "Phạm Ngũ Lão", "Trần Phú", "Bùi Viện", "Hàm Nghi", "Tôn Thất Thiệp"
        };

        // Create exactly 10 shipping addresses (1 per customer, if we have 10 customers)
        int addressCount = customerUsers.size();

        for (int i = 0; i < addressCount; i++) {
            User customer = customerUsers.get(i % customerUsers.size());

            // Select random city and district
            int cityIdx = i % cityDistricts.length;
            String city = cityDistricts[cityIdx][0];
            String district = cityDistricts[cityIdx][1 + (i % (cityDistricts[cityIdx].length - 1))];

            // Create a Vietnamese address
            int houseNumber = 10 + (i * 7) % 90;
            String street = streetNames[i % streetNames.length];
            String addressLine = "Số " + houseNumber + " " + street;

            ShippingAddresses address = ShippingAddresses.builder()
                    .addressLine(addressLine)
                    .addressCity(city)
                    .addressDistrict(district)
                    .addressIsDefault(true) // One address per customer, so make it default
                    .user(customer)
                    .build();

            addresses.add(address);
        }

        shippingAddressesRepository.saveAll(addresses);
        log.info("Created {} shipping addresses", addresses.size());
    }

    private void createPaymentMethods() {
        log.info("Creating payment methods...");

        List<PaymentMethods> methods = new ArrayList<>();

        // Create payment methods using the enum values
        methods.add(PaymentMethods.builder()
                .paymentMethodName(Constants.PaymentMethodEnum.CASH)
                .paymentMethodDescription("Thanh toán bằng tiền mặt tại quầy hoặc khi nhận hàng")
                .isActive(true)
                .build());

        methods.add(PaymentMethods.builder()
                .paymentMethodName(Constants.PaymentMethodEnum.PAYPAL)
                .paymentMethodDescription("Thanh toán trực tuyến qua cổng PayPal")
                .isActive(true)
                .build());

        methods.add(PaymentMethods.builder()
                .paymentMethodName(Constants.PaymentMethodEnum.VNPAY)
                .paymentMethodDescription("Thanh toán qua ví điện tử VN Pay")
                .isActive(true)
                .build());

        methods.add(PaymentMethods.builder()
                .paymentMethodName(Constants.PaymentMethodEnum.MOMO)
                .paymentMethodDescription("Thanh toán qua ví điện tử MoMo")
                .isActive(false)
                .build());

        methods.add(PaymentMethods.builder()
                .paymentMethodName(Constants.PaymentMethodEnum.ZALOPAY)
                .paymentMethodDescription("Thanh toán qua ví điện tử Zalo Pay")
                .isActive(false)
                .build());

        paymentMethodsRepository.saveAll(methods);
        log.info("Created {} payment methods", methods.size());
    }

    private String getRandomDigits() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            sb.append((int)(Math.random() * 10));
        }
        return sb.toString();
    }
}
