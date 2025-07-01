package com.se330.coffee_shop_management_backend.service;

import com.se330.coffee_shop_management_backend.entity.Setting;
import com.se330.coffee_shop_management_backend.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingService {
    private final SettingRepository settingRepository;

    public Setting create() {
        return settingRepository.save(Setting.builder().key("foo").value("bar").build());
    }
}
