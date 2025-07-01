package com.se330.coffee_shop_management_backend.util;

public class CreateSlug {
    public static String createSlug(String name) {
        return name.toLowerCase()
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
}
