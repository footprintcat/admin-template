package com.example.backend.common.ManageList.pojo.columnconfig;

import com.example.backend.common.ManageList.pojo.columnconfig.base.BaseColumnConfig;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 图片列配置类
 * 用于配置图片类型列的额外选项
 */
@Data
@Accessors(chain = true)
public class ImageColumnConfig extends BaseColumnConfig<ImageColumnConfig> {
    // 图片宽度
    private Integer width;
    // 图片高度
    private Integer height;

    @Override
    public boolean getIsValid() {
        // 检查图片宽度是否有效（如果设置了的话）
        if (width != null && width <= 0) {
            return false;
        }
        // 检查图片高度是否有效（如果设置了的话）
        if (height != null && height <= 0) {
            return false;
        }
        return true;
    }

    @Override
    protected ImageColumnConfig self() {
        return this; // 返回当前子类实例
    }
}