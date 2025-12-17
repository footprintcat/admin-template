package com.example.backend.common.ManageList.pojo.queryconfig;

import com.example.backend.common.ManageList.enums.DateQueryType;
import com.example.backend.common.ManageList.pojo.queryconfig.base.BaseQueryConfig;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 日期时间查询配置类
 * 用于配置日期时间类型查询条件的额外选项
 */
@Data
@Accessors(chain = true)
public class DateTimeQueryConfig extends BaseQueryConfig<DateTimeQueryConfig> {
    // 日期类型：DATETIME 日期时间, DATE 仅日期, TIME 仅时间
    private DateQueryType dateType = DateQueryType.DATETIME;

    @Override
    public boolean getIsValid() {
        // 检查日期类型是否有效
        return dateType != null;
    }

    @Override
    protected DateTimeQueryConfig self() {
        return this; // 返回当前子类实例
    }
}
