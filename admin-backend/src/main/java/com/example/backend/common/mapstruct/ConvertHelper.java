package com.example.backend.common.mapstruct;

import org.springframework.stereotype.Component;

@Component
public class ConvertHelper {
    public Boolean intToBoolean(Integer value) {
        if (value == null) {
            return null;
        }
        return value == 1;
    }

    public Integer booleanToInt(Boolean value) {
        if (value == null) {
            return null;
        }
        return value ? 1 : 0;
    }
}
