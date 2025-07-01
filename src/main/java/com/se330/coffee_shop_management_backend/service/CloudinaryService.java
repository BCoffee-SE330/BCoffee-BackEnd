package com.se330.coffee_shop_management_backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Transactional
    public Map uploadFile(MultipartFile file, String folder) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", folder));
    }

    @Transactional
    public Map deleteFile(String url) throws IOException {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        int uploadIndex = url.indexOf("/upload/");
        if (uploadIndex == -1) {
            throw new IllegalArgumentException("Invalid Cloudinary URL format");
        }

        String afterUpload = url.substring(uploadIndex + 8);

        if (afterUpload.startsWith("v")) {
            int versionEndIndex = afterUpload.indexOf("/");
            if (versionEndIndex != -1) {
                afterUpload = afterUpload.substring(versionEndIndex + 1);
            }
        }

        int extensionIndex = afterUpload.lastIndexOf(".");
        if (extensionIndex != -1) {
            afterUpload = afterUpload.substring(0, extensionIndex);
        }

        String publicId = afterUpload;

        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public String getAvatarDefault() {
        return System.getenv("DEFAULT_USER_AVATAR_URL");
    }

    public String getProductDefault() {
        return System.getenv("DEFAULT_PRODUCT_IMAGE_URL");
    }
}
