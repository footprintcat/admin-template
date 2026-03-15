package com.example.backend.common.utils;

import com.example.backend.modules.system.model.dto.export.UserExportDto;
import org.apache.fesod.sheet.annotation.ExcelIgnore;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导出工具类 - 用于生成前端导出所需的 head 信息
 * <p>
 * 通过解析 DTO 类上的注解（@ExcelProperty、@ColumnWidth、@ExcelIgnore）来自动生成列信息
 *
 * @since 2026-03-15
 */
public class ManageListFrontExportBuilder {

    public static <T> Map<String, Object> build(@NotNull String fileName, @NotNull String sheetName,
                                                @NotNull Class<T> clazz, @NotNull Collection<T> data) {
        // 构建前端期望的 ExportResult 格式
        HashMap<String, Object> result = new HashMap<>();
        result.put("fileName", fileName);
        result.put("sheetName", sheetName);

        // 通过注解自动生成 head 信息
        result.put("head", buildHead(clazz));

        // data 数据
        result.put("data", data);

        return result;
    }

    /**
     * 构建导出列信息（head）
     * <p>
     * 解析 DTO 类上的注解，生成前端需要的 columns 数组
     *
     * @param clazz DTO 类
     * @return 包含 showHead 和 columns 的 Map
     */
    private static Map<String, Object> buildHead(Class<?> clazz) {
        Map<String, Object> head = new HashMap<>();
        head.put("showHead", true);

        List<Map<String, Object>> columns = new ArrayList<>();

        // 获取所有字段（按声明顺序）
        List<Field> fields = getFieldsInOrder(clazz);

        for (Field field : fields) {
            // 忽略标记了 @ExcelIgnore 的字段
            if (field.isAnnotationPresent(ExcelIgnore.class)) {
                continue;
            }

            Map<String, Object> column = new HashMap<>();

            // 字段名
            column.put("field", field.getName());

            // 列标题：优先使用 @ExcelProperty 的 value，否则使用字段名
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                String[] fieldNames = excelProperty.value();
                if (fieldNames != null && fieldNames.length > 0) {
                    String fieldName = fieldNames[0];
                    if (fieldName != null && !fieldName.isEmpty()) {
                        column.put("fieldName", fieldName);
                    } else {
                        column.put("fieldName", field.getName());
                    }
                }
            } else {
                column.put("fieldName", field.getName());
            }

            // 列宽：优先使用 @ColumnWidth，否则使用默认值 15
            if (field.isAnnotationPresent(ColumnWidth.class)) {
                ColumnWidth columnWidth = field.getAnnotation(ColumnWidth.class);
                column.put("columnWidth", columnWidth.value());
            } else {
                column.put("columnWidth", 15);
            }

            columns.add(column);
        }

        head.put("columns", columns);
        return head;
    }

    /**
     * 获取所有字段（按 @ExcelProperty.index 排序，index 相同则按声明顺序）
     */
    @NotNull
    private static List<Field> getFieldsInOrder(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        // 收集所有字段（包括父类）
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }

        // 按 @ExcelProperty.index 排序，默认为 Integer.MAX_VALUE（排在后面）
        return fields.stream()
                .sorted(Comparator.comparingInt(f -> {
                    if (f.isAnnotationPresent(ExcelProperty.class)) {
                        ExcelProperty excelProperty = f.getAnnotation(ExcelProperty.class);
                        return excelProperty.index();
                    }
                    return Integer.MAX_VALUE;
                }))
                .collect(Collectors.toList());
    }
}
