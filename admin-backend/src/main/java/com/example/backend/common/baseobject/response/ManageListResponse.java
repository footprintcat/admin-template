package com.example.backend.common.baseobject.response;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

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

    // 使用 Integer 类型返回，因为 Long 类型会被 JacksonConfig 全局处理成字符串
    private Integer total;
    private Collection<T> list;

    public ManageListResponse<T> setTotal(@NotNull Long total) {
        this.total = Math.toIntExact(total);
        return this;
    }

    public ManageListResponse<T> setTotal(@NotNull Integer total) {
        this.total = total;
        return this;
    }

}
