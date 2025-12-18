package com.example.backend.common.baseobject.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;

/**
 * 后台管理统一返回结构
 *
 * @param <T>
 */
@Data
@Accessors(chain = true)
public class ManageListResponse<T> {

    private ManageListResponse() {
    }

    public static <T> ManageListResponse<T> create() {
        return new ManageListResponse<>();
    }

    private Long total;
    private Collection<T> list;

}
