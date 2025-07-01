package com.se330.coffee_shop_management_backend.util;

import org.springframework.data.domain.Sort.Direction;

public abstract class AbstractBaseSortDirection {
    /**
     * @param sort String
     * @return Sort.Direction
     */
    protected static Direction getDirection(final String sort) {
        if ("desc".equalsIgnoreCase(sort)) {
            return Direction.DESC;
        }

        return Direction.ASC;
    }
}
