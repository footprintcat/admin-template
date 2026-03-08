package com.example.backend.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.fesod.sheet.ExcelWriter;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.write.metadata.WriteSheet;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * 导出 Excel 工具类
 *
 * @since 2026-03-08
 */
public class ExportExcelUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");

    public static String getFormattedDateInFileName() {
        // 当前时间
        return LocalDateTime.now().format(FORMATTER);
    }

    public static String getFormattedDateInFileName(@NotNull LocalDateTime now) {
        return now.format(FORMATTER);
    }

    /**
     * 设置响应头
     *
     * @param response
     * @param fileName
     */
    public static void setResponseHeader(@NotNull HttpServletResponse response, @NotNull String fileName) {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

        // 1. 设置响应头，告诉浏览器这是一个需要下载的Excel文件
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName + "; filename*=UTF-8''" + encodedFileName);

        // data-filename 用于前端获取下载文件的文件名；Access-Control-Expose-Headers 暴露后前端才能获取到
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition, data-filename");
        response.setHeader("data-filename", encodedFileName); // 包含中文时会加不上
    }

    /**
     * 写入 excel
     *
     * @param dataList
     * @param clazz
     * @param sheetName
     * @param outputStream
     * @param <T>
     */
    public static <T> void exportToStream(@NotNull Collection<T> dataList, Class<T> clazz, String sheetName, OutputStream outputStream) {
        try (ExcelWriter excelWriter = FesodSheet.write(outputStream, clazz)
                .build()) {
            WriteSheet writeSheet = FesodSheet.writerSheet(sheetName).build();
            excelWriter.write(dataList, writeSheet);

            // 去除导出文件的作者、应用程序属性
            Workbook workbook = excelWriter.writeContext().writeWorkbookHolder().getWorkbook();
            setAuthor(workbook, "");

            // 冻结首行
            Sheet sheet = excelWriter.writeContext().writeSheetHolder().getSheet();
            sheet.createFreezePane(0, 1);
        }
    }

    // 替换默认的 Apache POI
    private static void setAuthor(Workbook workbook, String author) {
        if (workbook instanceof XSSFWorkbook xssfWorkbook) { // .xlsx
            setXSSFAuthor(xssfWorkbook, author);
        } else if (workbook instanceof SXSSFWorkbook sxssfWorkbook) { // 流式写入.xlsx
            // SXSSFWorkbook 底层是 XSSFWorkbook
            setXSSFAuthor(sxssfWorkbook.getXSSFWorkbook(), author);
        } else if (workbook instanceof HSSFWorkbook) { // .xls
            setHSSFAuthor((HSSFWorkbook) workbook, author);
        } else {
            // 未知类型，可记录日志或忽略
            throw new IllegalArgumentException("Unsupported workbook type: " + workbook.getClass());
        }
    }

    // 对于 XSSFWorkbook (xlsx)
    private static void setXSSFAuthor(XSSFWorkbook xssfWorkbook, String author) {
        POIXMLProperties props = xssfWorkbook.getProperties();
        POIXMLProperties.CoreProperties coreProps = props.getCoreProperties();
        coreProps.setCreator(author); // 作者
        coreProps.setLastModifiedByUser(""); // 修改者 (可选)
        // coreProps.setCreated(Optional.of(new Date())); // 可选
        // 也可以修改应用程序名称
        POIXMLProperties.ExtendedProperties extProps = props.getExtendedProperties();
        extProps.getUnderlyingProperties().setApplication(""); // 应用程序名称 (可选)
    }

    // 对于 HSSFWorkbook (xls)
    private static void setHSSFAuthor(HSSFWorkbook hssfWorkbook, String author) {
        SummaryInformation summary = hssfWorkbook.getSummaryInformation();
        if (summary != null) {
            summary.setAuthor(author); // 作者
            summary.setApplicationName(""); // 应用程序名称 (可选)
        }
    }
}
