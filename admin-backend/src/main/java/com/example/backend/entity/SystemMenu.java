package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-12
 */
@Getter
@Setter
@Schema(name = "SystemMenu", description = "")
@TableName("system_menu")
public class SystemMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "父级菜单项id")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "菜单级别")
    @TableField("`level`")
    private Integer level;

    @Schema(description = "菜单id")
    @TableField("menu_id")
    private String menuId;

    @Schema(description = "菜单全路径名称")
    @TableField("menu_full_name")
    private String menuFullName;

    @Schema(description = "菜单名称")
    @TableField("menu_name")
    private String menuName;

    @Schema(description = "菜单项顺序")
    @TableField("sequence")
    private Integer sequence;

    @Schema(description = "是否隐藏菜单项（1：隐藏，0：不隐藏）")
    @TableField("is_hide")
    private Integer isHide;

    @Schema(description = "最后更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;
}
