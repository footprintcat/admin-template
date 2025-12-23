package com.example.backend.common.ManageList.pojo;

import com.example.backend.common.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 列表管理信息
 * 用于存储管理列表页面的基本信息
 */
@Getter
@Accessors(chain = true)
public class ManageListInfo {

    /**
     * 当前管理页面名称
     */
    @Setter
    private String pageName;

    // 是否显示批量操作按钮
    private boolean showBatchButton = true;
    // 是否显示刷新按钮
    private boolean showRefreshButton = true;
    // 是否显示搜索按钮
    private boolean showSearchButton = true;
    // 是否显示重置按钮
    private boolean showResetButton = true;
    // 是否显示导出按钮
    private boolean showExportButton = false;
    // 是否显示导入按钮
    private boolean showImportButton = false;
    // 是否显示新增按钮
    private boolean showAddButton = true;

    private ManageListInfo() {
    }

    public static ManageListInfo create() {
        return new ManageListInfo();
    }

    /**
     * 设置是否显示批量操作按钮
     *
     * @param showBatchButton 是否显示批量操作按钮
     * @return this
     */
    public ManageListInfo setShowBatchButton(boolean showBatchButton) {
        this.showBatchButton = showBatchButton;
        return this;
    }

    /**
     * 设置是否显示刷新按钮
     *
     * @param showRefreshButton 是否显示刷新按钮
     * @return this
     */
    public ManageListInfo setShowRefreshButton(boolean showRefreshButton) {
        this.showRefreshButton = showRefreshButton;
        return this;
    }

    /**
     * 设置是否显示搜索按钮
     *
     * @param showSearchButton 是否显示搜索按钮
     * @return this
     */
    public ManageListInfo setShowSearchButton(boolean showSearchButton) {
        this.showSearchButton = showSearchButton;
        return this;
    }

    /**
     * 设置是否显示重置按钮
     *
     * @param showResetButton 是否显示重置按钮
     * @return this
     */
    public ManageListInfo setShowResetButton(boolean showResetButton) {
        this.showResetButton = showResetButton;
        return this;
    }

    /**
     * 设置是否显示导出按钮
     *
     * @param showExportButton 是否显示导出按钮
     * @return this
     */
    public ManageListInfo setShowExportButton(boolean showExportButton) {
        this.showExportButton = showExportButton;
        return this;
    }

    /**
     * 设置是否显示导入按钮
     *
     * @param showImportButton 是否显示导入按钮
     * @return this
     */
    public ManageListInfo setShowImportButton(boolean showImportButton) {
        this.showImportButton = showImportButton;
        return this;
    }

    /**
     * 设置是否显示新增按钮
     *
     * @param showAddButton 是否显示新增按钮
     * @return this
     */
    public ManageListInfo setShowAddButton(boolean showAddButton) {
        this.showAddButton = showAddButton;
        return this;
    }

    public static void checkValidOrThrow(@NotNull final String lowerCaseCallerName, @NotNull final String setFunctionName,
                                         @Nullable ManageListInfo info) {
        final String className = ManageListInfo.class.getSimpleName();
        char[] charArray = className.toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        final String lowerCaseClassName = new String(charArray);

        if (info == null) {
            throw new IllegalArgumentException(lowerCaseClassName + " is not set, please call " + lowerCaseCallerName + "." + setFunctionName + "(" + lowerCaseClassName + ")");
        }

        if (!StringUtils.isNotEmpty(info.getPageName())) {
            throw new IllegalArgumentException(lowerCaseClassName + " is not valid, please call " + lowerCaseClassName + ".setPageName(pageName);");
        }
    }
}
