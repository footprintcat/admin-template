package com.example.backend.common.ManageList.pojo.columnconfig;

import com.example.backend.common.ManageList.pojo.columnconfig.base.BaseColumnConfig;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 文本列配置类
 * 用于配置文本类型列的额外选项
 */
@Data
@Accessors(chain = true)
public class TextColumnConfig extends BaseColumnConfig<TextColumnConfig> {
    // 过长时是否展示省略号，默认为false
    private boolean longTextEllipsis = false;
    // 是否使用工具提示，默认为false
    private boolean useTooltip = false;
    // 是否作为标签显示，默认为false
    private boolean showAsTag = false;
    // 标签类型：default, success, warn, error, grey
    private String tagType;

    /**
     * 设置是否作为标签显示，并指定标签类型
     *
     * @param showAsTag 是否作为标签显示
     * @param tagType   标签类型
     * @return 当前配置对象
     */
    public TextColumnConfig setShowAsTag(boolean showAsTag, String tagType) {
        this.showAsTag = showAsTag;
        this.tagType = tagType;
        return this;
    }

    @Override
    public boolean getIsValid() {
        // 检查列宽度是否有效（如果设置了的话）
        final var columnWidth = super.getColumnWidth();
        if (columnWidth != null && columnWidth <= 0) {
            return false;
        }
        // 检查标签类型是否有效（如果设置了showAsTag为true）
        if (showAsTag && (tagType == null || !tagType.matches("^(default|success|warn|error|grey)$"))) {
            return false;
        }
        return true;
    }

    @Override
    protected TextColumnConfig self() {
        return this; // 返回当前子类实例
    }
}