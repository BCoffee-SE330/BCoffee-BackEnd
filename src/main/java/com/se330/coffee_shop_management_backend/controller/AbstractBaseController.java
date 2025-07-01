package com.se330.coffee_shop_management_backend.controller;

import com.se330.coffee_shop_management_backend.exception.BadRequestException;
import com.se330.coffee_shop_management_backend.service.MessageSourceService;

import java.util.Arrays;

public abstract class AbstractBaseController {
    /**
     * Sort column check.
     *
     * @param messageSourceService MessageSourceService
     * @param sortColumns          String[]
     * @param sortBy               String
     */
    protected void sortColumnCheck(final MessageSourceService messageSourceService,
                                   final String[] sortColumns,
                                   final String sortBy) {
        if (sortBy != null && !Arrays.asList(sortColumns).contains(sortBy)) {
            throw new BadRequestException(messageSourceService.get("invalid_sort_column"));
        }
    }
}
