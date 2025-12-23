package com.example.backend.common.ManageList.pojo;

import com.example.backend.common.ManageList.enums.ColumnType;
import com.example.backend.common.ManageList.enums.DateDisplayType;
import com.example.backend.common.ManageList.enums.DateValueType;
import com.example.backend.common.ManageList.pojo.columnconfig.DateTimeColumnConfig;
import com.example.backend.common.ManageList.pojo.columnconfig.ImageColumnConfig;
import com.example.backend.common.ManageList.pojo.columnconfig.TextColumnConfig;
import com.example.backend.common.ManageList.pojo.columnconfig.base.BaseColumnConfig;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 管理列表列配置类
 * 用于配置表格的列信息和展示方式
 */
@Data
@Accessors(chain = true)
public class ManageListColumn {

    // 列的配置列表
    private List<Map<String, Object>> columns = new ArrayList<>();

    private ManageListColumn() {
    }

    /**
     * 创建一个新的ManageListColumn实例
     *
     * @return ManageListColumn实例
     */
    public static ManageListColumn create() {
        return new ManageListColumn();
    }

    private <T extends BaseColumnConfig<?>> Map<String, Object> createBaseColumnMap(
            @NotNull String field, @NotNull String fieldName, @NotNull ColumnType columnType,
            @Nullable Map<String, Object> fieldMapper,
            @Nullable Function<T, T> configFunc, @Nullable T configInstance
    ) {
        Map<String, Object> columnMap = new java.util.HashMap<>();
        columnMap.put("field", field); // 字段名
        columnMap.put("fieldName", fieldName); // 显示名称
        columnMap.put("columnType", columnType.getCode()); // 列类型
        columnMap.put("fieldMapper", fieldMapper); // 字段映射器 可为null

        // 配置函数 & 配置实例
        if (configFunc != null && configInstance != null) {
            T config = configFunc.apply(configInstance);
            // 检查配置是否有效
            if (!config.getIsValid()) {
                throw new IllegalArgumentException(config.getClass().getSimpleName() + "column is not valid. " +
                                                   "field: " + field + ", " + "fieldName: " + fieldName + "columnType: " + columnType);
            }

            columnMap.put("extra", config);
        } else {
            columnMap.put("extra", Collections.emptyMap());
        }
        return columnMap;
    }

    // ===== 添加列通用方法 =====

    /**
     * 添加指定类型的列
     *
     * @param field      字段名
     * @param fieldName  显示名称
     * @param columnType 列类型
     * @return 当前实例
     */
    public ManageListColumn add(@NotNull String field, @NotNull String fieldName, @NotNull ColumnType columnType) {
        return add(field, fieldName, columnType, null, null, null);
    }

    /**
     * 添加字段映射列
     *
     * @param field       字段名
     * @param fieldName   显示名称
     * @param fieldMapper 字段映射器
     * @return 当前实例
     */
    public ManageListColumn add(@NotNull String field, @NotNull String fieldName, @NotNull ColumnType columnType,
                                Map<String, Object> fieldMapper) {
        return add(field, fieldName, columnType, fieldMapper, null, null);
    }

    /**
     * 添加带配置的列
     *
     * @param field          字段名
     * @param fieldName      显示名称
     * @param columnType     列类型
     * @param configFunc     配置函数
     * @param configInstance 配置实例
     * @param <T>            配置类型
     * @return 当前实例
     */
    public <T extends BaseColumnConfig<?>> ManageListColumn add(
            @NotNull String field, @NotNull String fieldName, @NotNull ColumnType columnType,
            @Nullable Function<T, T> configFunc, @Nullable T configInstance
    ) {
        return add(field, fieldName, columnType, null, configFunc, configInstance);
    }

    /**
     * 添加带配置的列
     *
     * @param field          字段名
     * @param fieldName      显示名称
     * @param columnType     列类型
     * @param fieldMapper    字段映射器
     * @param configFunc     配置函数
     * @param configInstance 配置实例
     * @param <T>            配置类型
     * @return 当前实例
     */
    public <T extends BaseColumnConfig<?>> ManageListColumn add(
            @NotNull String field, @NotNull String fieldName, @NotNull ColumnType columnType,
            @Nullable Map<String, Object> fieldMapper,
            @Nullable Function<T, T> configFunc, @Nullable T configInstance
    ) {
        Map<String, Object> columnMap = createBaseColumnMap(field, fieldName, columnType, fieldMapper, configFunc, configInstance);
        columns.add(columnMap);
        return this;
    }

    // ===== 文本列 =====

    /**
     * 添加文本列
     */
    public ManageListColumn text(@NotNull String field, @NotNull String fieldName) {
        return text(field, fieldName, null, null);
    }

    public ManageListColumn text(@NotNull String field, @NotNull String fieldName,
                                 @Nullable Map<String, Object> fieldMapper) {
        return text(field, fieldName, fieldMapper, null);
    }

    public ManageListColumn text(@NotNull String field, @NotNull String fieldName,
                                 Function<TextColumnConfig, TextColumnConfig> textExtra) {
        return text(field, fieldName, null, textExtra);
    }

    /**
     * 添加文本列
     *
     * @param field     字段名
     * @param fieldName 显示名称
     * @return 当前实例
     */
    public ManageListColumn text(@NotNull String field, @NotNull String fieldName,
                                 @Nullable Map<String, Object> fieldMapper,
                                 @Nullable Function<TextColumnConfig, TextColumnConfig> textExtra) {
        return add(field, fieldName, ColumnType.TEXT, fieldMapper, textExtra, new TextColumnConfig());
    }

    // ===== 日期时间列 =====

    /**
     * 添加日期时间列
     *
     * @param dateValueType   日期值类型
     * @param dateDisplayType 日期显示类型
     * @return 当前实例
     */
    public ManageListColumn datetime(@NotNull String field, @NotNull String fieldName,
                                     @NotNull DateValueType dateValueType, @NotNull DateDisplayType dateDisplayType) {
        return datetime(field, fieldName, dateValueType, dateDisplayType, null, null);
    }

    public ManageListColumn datetime(@NotNull String field, @NotNull String fieldName,
                                     @NotNull DateValueType dateValueType, @NotNull DateDisplayType dateDisplayType,
                                     @Nullable Map<String, Object> fieldMapper) {
        return datetime(field, fieldName, dateValueType, dateDisplayType, fieldMapper, null);
    }

    public ManageListColumn datetime(@NotNull String field, @NotNull String fieldName,
                                     @NotNull DateValueType dateValueType, @NotNull DateDisplayType dateDisplayType,
                                     @Nullable Function<DateTimeColumnConfig, DateTimeColumnConfig> dateTimeExtra) {
        return datetime(field, fieldName, dateValueType, dateDisplayType, null, dateTimeExtra);
    }

    /**
     * 添加日期时间列
     *
     * @param field           字段名
     * @param fieldName       显示名称
     * @param dateValueType   日期值类型
     * @param dateDisplayType 日期显示类型
     * @param fieldMapper     字段映射器
     * @param dateTimeExtra   额外配置函数
     * @return 当前实例
     */
    public ManageListColumn datetime(
            @NotNull String field, @NotNull String fieldName,
            @NotNull DateValueType dateValueType, @NotNull DateDisplayType dateDisplayType,
            @Nullable Map<String, Object> fieldMapper,
            @Nullable Function<DateTimeColumnConfig, DateTimeColumnConfig> dateTimeExtra
    ) {
        Map<String, Object> columnMap = createBaseColumnMap(field, fieldName, ColumnType.DATETIME, fieldMapper, dateTimeExtra, new DateTimeColumnConfig());
        columnMap.put("dateValueType", dateValueType);
        columnMap.put("dateDisplayType", dateDisplayType);
        // columnMap.put("dateDisplayType", Objects.requireNonNullElse(dateDisplayType, DateDisplayType.DATE_TIME));
        columns.add(columnMap);
        return this;
    }

    // ===== 图片列 =====

    public ManageListColumn image(@NotNull String field, @NotNull String fieldName,
                                  @Nullable Map<String, Object> fieldMapper,
                                  @NotNull Function<ImageColumnConfig, ImageColumnConfig> imageExtra) {
        return add(field, fieldName, ColumnType.IMAGE, fieldMapper, imageExtra, new ImageColumnConfig());
    }

    /**
     * 检查配置是否有效，无效则抛出异常
     *
     * @param lowerCaseCallerName 调用者类名（小写）
     * @param setFunctionName     设置函数名
     * @param info                ManageListColumn对象
     */
    public static void checkValidOrThrow(@NotNull final String lowerCaseCallerName, @NotNull final String setFunctionName,
                                         @Nullable ManageListColumn info) {
        final String className = ManageListColumn.class.getSimpleName();
        char[] charArray = className.toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        final String lowerCaseClassName = new String(charArray);

        if (info == null) {
            throw new IllegalArgumentException(lowerCaseClassName + " is not set, please call " + lowerCaseCallerName + "." + setFunctionName + "(" + lowerCaseClassName + ")");
        }

        if (info.getColumns() == null || info.getColumns().isEmpty()) {
            throw new IllegalArgumentException(lowerCaseClassName + " is not valid, please call " + lowerCaseClassName + ".add(field, fieldName, ...);");
        }
    }
}
