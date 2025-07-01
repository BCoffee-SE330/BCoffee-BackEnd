package com.se330.coffee_shop_management_backend.repository;

import com.se330.coffee_shop_management_backend.entity.UserRecipientToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRecipientTokenRepository extends JpaRepository<UserRecipientToken, UUID>, JpaSpecificationExecutor<UserRecipientToken> {
    UserRecipientToken findByFCMRecipientTokenAndUser_Id(String fcmRecipientToken, UUID userId);
}
