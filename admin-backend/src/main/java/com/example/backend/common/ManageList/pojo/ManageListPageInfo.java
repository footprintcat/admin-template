package com.example.backend.common.ManageList.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 列表配置类
 * 用于配置表格的分页、排序等特性
 */
@Data
@Accessors(chain = true)
public class ManageListPageInfo {

    // 是否启用分页
    private boolean pagination = true;
    // 每页显示条数
    private int pageSize = 10;
    // 分页大小选项
    private int[] pageSizeOptions = {10, 20, 50, 100};
    // 是否显示总数
    private boolean showTotal = true;
    // 是否显示跳转功能
    private boolean showJumper = true;
    // 是否显示尺寸选择器
    private boolean showSizeChanger = true;
    // 是否显示快速跳转
    private boolean showQuickJumper = false;
    // 是否允许排序
    private boolean sortable = true;
    // 默认排序字段
    private String defaultSortField;
    // 默认排序顺序（ascend或descend）
    private String defaultSortOrder;
    // 是否启用复选框
    private boolean checkable = true;
    // 是否支持全选
    private boolean checkAll = true;
    // 是否支持行选择
    private boolean rowSelectable = false;
    // 是否支持行操作
    private boolean showActionButtons = true;
    // 每行操作按钮数量
    private int actionButtonCount = 3;

    private ManageListPageInfo() {
    }

    /**
     * 创建一个新的ListConfig实例
     *
     * @return ListConfig实例
     */
    public static ManageListPageInfo create() {
        return new ManageListPageInfo();
    }

    /**
     * 设置是否启用分页
     *
     * @param pagination 是否启用分页
     * @return 当前实例
     */
    public ManageListPageInfo setPagination(boolean pagination) {
        this.pagination = pagination;
        return this;
    }

    /**
     * 设置每页显示条数
     *
     * @param pageSize 每页显示条数
     * @return 当前实例
     */
    public ManageListPageInfo setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * 设置分页大小选项
     *
     * @param pageSizeOptions 分页大小选项
     * @return 当前实例
     */
    public ManageListPageInfo setPageSizeOptions(int[] pageSizeOptions) {
        this.pageSizeOptions = pageSizeOptions;
        return this;
    }

    /**
     * 设置是否显示总数
     *
     * @param showTotal 是否显示总数
     * @return 当前实例
     */
    public ManageListPageInfo setShowTotal(boolean showTotal) {
        this.showTotal = showTotal;
        return this;
    }

    /**
     * 设置是否显示跳转功能
     *
     * @param showJumper 是否显示跳转功能
     * @return 当前实例
     */
    public ManageListPageInfo setShowJumper(boolean showJumper) {
        this.showJumper = showJumper;
        return this;
    }

    /**
     * 设置是否显示尺寸选择器
     *
     * @param showSizeChanger 是否显示尺寸选择器
     * @return 当前实例
     */
    public ManageListPageInfo setShowSizeChanger(boolean showSizeChanger) {
        this.showSizeChanger = showSizeChanger;
        return this;
    }

    /**
     * 设置是否显示快速跳转
     *
     * @param showQuickJumper 是否显示快速跳转
     * @return 当前实例
     */
    public ManageListPageInfo setShowQuickJumper(boolean showQuickJumper) {
        this.showQuickJumper = showQuickJumper;
        return this;
    }

    /**
     * 设置是否允许排序
     *
     * @param sortable 是否允许排序
     * @return 当前实例
     */
    public ManageListPageInfo setSortable(boolean sortable) {
        this.sortable = sortable;
        return this;
    }

    /**
     * 设置默认排序字段
     *
     * @param defaultSortField 默认排序字段
     * @return 当前实例
     */
    public ManageListPageInfo setDefaultSortField(String defaultSortField) {
        this.defaultSortField = defaultSortField;
        return this;
    }

    /**
     * 设置默认排序顺序
     *
     * @param defaultSortOrder 默认排序顺序（ascend或descend）
     * @return 当前实例
     */
    public ManageListPageInfo setDefaultSortOrder(String defaultSortOrder) {
        this.defaultSortOrder = defaultSortOrder;
        return this;
    }

    /**
     * 设置是否启用复选框
     *
     * @param checkable 是否启用复选框
     * @return 当前实例
     */
    public ManageListPageInfo setCheckable(boolean checkable) {
        this.checkable = checkable;
        return this;
    }

    /**
     * 设置是否支持全选
     *
     * @param checkAll 是否支持全选
     * @return 当前实例
     */
    public ManageListPageInfo setCheckAll(boolean checkAll) {
        this.checkAll = checkAll;
        return this;
    }

    /**
     * 设置是否支持行选择
     *
     * @param rowSelectable 是否支持行选择
     * @return 当前实例
     */
    public ManageListPageInfo setRowSelectable(boolean rowSelectable) {
        this.rowSelectable = rowSelectable;
        return this;
    }

    /**
     * 设置是否支持行操作
     *
     * @param showActionButtons 是否支持行操作
     * @return 当前实例
     */
    public ManageListPageInfo setShowActionButtons(boolean showActionButtons) {
        this.showActionButtons = showActionButtons;
        return this;
    }

    /**
     * 设置每行操作按钮数量
     *
     * @param actionButtonCount 每行操作按钮数量
     * @return 当前实例
     */
    public ManageListPageInfo setActionButtonCount(int actionButtonCount) {
        this.actionButtonCount = actionButtonCount;
        return this;
    }

    /**
     * 检查并抛出异常
     *
     * @param builderName 构建器名称
     * @param methodName  方法名称
     * @param config      配置对象
     */
    public static void checkValidOrThrow(@NotNull final String builderName, @NotNull final String methodName, @Nullable ManageListPageInfo config) {
        if (config == null) {
            throw new RuntimeException(builderName + "." + methodName + " must not be null!");
        }

        if (config.getPageSize() <= 0) {
            throw new RuntimeException(builderName + "." + methodName + ".pageSize must be greater than 0!");
        }

        if (config.getPageSizeOptions() == null || config.getPageSizeOptions().length == 0) {
            throw new RuntimeException(builderName + "." + methodName + ".pageSizeOptions must not be null or empty!");
        }

        if (config.getDefaultSortOrder() != null && !config.getDefaultSortOrder().equals("ascend") && !config.getDefaultSortOrder().equals("descend")) {
            throw new RuntimeException(builderName + "." + methodName + ".defaultSortOrder must be 'ascend' or 'descend'!");
        }

        if (config.getDefaultSortOrder() != null && config.getDefaultSortField() == null) {
            throw new RuntimeException(builderName + "." + methodName + ".defaultSortField must be set when defaultSortOrder is set!");
        }

        if (config.getActionButtonCount() < 0) {
            throw new RuntimeException(builderName + "." + methodName + ".actionButtonCount must be greater than or equal to 0!");
        }
    }
}