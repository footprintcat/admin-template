package com.example.backend.modules.system.model.dto.export;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.fesod.sheet.annotation.ExcelIgnore;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.annotation.write.style.ColumnWidth;

@Getter
@Setter
@EqualsAndHashCode
public class UserExportDto {

    @ColumnWidth(10)
    @ExcelProperty("用户id")
    private Long id;

    @ColumnWidth(15)
    @ExcelProperty("用户名")
    private String username;

    @ColumnWidth(16)
    @ExcelProperty("昵称")
    private String nickname;

    @ExcelIgnore
    private Long roleId;

    @ColumnWidth(12)
    @ExcelProperty("电话")
    private String telephone;

    @ColumnWidth(12)
    @ExcelProperty("状态")
    private String statusName;

}
