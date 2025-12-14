package com.example.backend.modules.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.example.backend.common.Enums.system.privilege.SystemPrivilegeEntityTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统权限表
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-12
 */
@Getter
@Setter
@Schema(name = "SystemPrivilege", description = "系统权限表")
@TableName("system_privilege")
public class SystemPrivilege implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "对象类型（user-用户；role-角色）")
    @TableField("entity_type")
    private SystemPrivilegeEntityTypeEnum entityType;

    @Schema(description = "对象id")
    @TableField("entity_id")
    private Long entityId;

    @Schema(description = "所属模块")
    @TableField("module")
    private String module;

    @Schema(description = "菜单code（例如 foo-bar.bar-foo，不得包含 : 符号）")
    @TableField("menu_code")
    private String menuCode;

    @Schema(description = "权限code（view_tab-查看tab权限；read-读取权限；add-新增权限；edit-编辑权限；delete-删除权限；export-导出权限）")
    @TableField("privilege_code")
    private String privilegeCode;

    @Schema(description = "租户id")
    @TableField("tenant_id")
    private Long tenantId;

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
