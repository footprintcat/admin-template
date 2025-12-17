package com.example.backend.common.ManageList.pojo.columnconfig;

import com.example.backend.common.ManageList.pojo.columnconfig.base.BaseColumnConfig;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 日期时间列配置类
 * 用于配置日期时间类型列的额外选项
 */
@Data
@Accessors(chain = true)
public class DateTimeColumnConfig extends BaseColumnConfig<DateTimeColumnConfig> {

    @Override
    public boolean getIsValid() {
        return true;
    }

    @Override
    protected DateTimeColumnConfig self() {
        return this; // 返回当前子类实例
    }
}
