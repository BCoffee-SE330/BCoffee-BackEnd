package com.se330.coffee_shop_management_backend.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateTrackingNumber {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    public static String createTrackingNumber(String name) {
        return name.toUpperCase() +
                "-" +
                LocalDateTime.now().format(FORMATTER) +
                "-" +
                System.currentTimeMillis();
    }
}