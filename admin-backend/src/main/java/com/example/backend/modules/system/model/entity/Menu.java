package com.example.backend.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-12
 */
@Getter
@Setter
@Schema(name = "Menu", description = "系统菜单表")
@TableName("system_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "父菜单id")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "菜单级别")
    @TableField("`level`")
    private Integer level;

    @Schema(description = "菜单类型（directory-分组；menu-菜单；action-操作(页面中功能或按钮)）")
    @TableField("menu_type")
    private String menuType;

    @Schema(description = "菜单code（例如 system:foo-bar）")
    @TableField("menu_code")
    private String menuCode;

    @Schema(description = "操作code（例如：export）")
    @TableField("action_code")
    private String actionCode;

    @Schema(description = "菜单名称")
    @TableField("menu_name")
    private String menuName;

    @Schema(description = "菜单URL路径（无页面的分组菜单项为NULL）")
    @TableField("menu_path")
    private String menuPath;

    @Schema(description = "菜单项顺序")
    @TableField("sort_order")
    private Integer sortOrder;

    @Schema(description = "是否允许编辑（系统菜单请置为0，避免误操作导致后台页面无法正常展示）")
    @TableField("can_edit")
    private Integer canEdit;

    @Schema(description = "是否隐藏菜单项（1：隐藏，0：不隐藏）")
    @TableField("is_hide")
    private Integer isHide;

    @Schema(description = "创建人")
    @TableField("create_by")
    private Long createBy;

    @Schema(description = "更新人")
    @TableField("update_by")
    private Long updateBy;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "最后更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除")
    @TableField("delete_time")
    @TableLogic
    private LocalDateTime deleteTime;

    @Schema(description = "版本号（乐观锁）")
    @TableField("version")
    @Version
    private Long version;
}
