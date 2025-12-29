package com.example.backend.common.baseobject.request;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class SortColumnRequestItem {

    @Nonnull
    private String field;

    @Nullable
    private String order;

    public boolean isDescSort() {
        return "descending".equals(this.order);
    }

}
