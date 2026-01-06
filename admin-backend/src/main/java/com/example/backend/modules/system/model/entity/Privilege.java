package com.example.backend.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.example.backend.modules.system.enums.privilege.PrivilegeGrantTypeEnum;
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
 * @since 2025-12-14
 */
@Getter
@Setter
@Schema(name = "Privilege", description = "系统权限表")
@TableName("system_privilege")
public class Privilege implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "对象类型（identity-身份；role-角色）")
    @TableField("entity_type")
    private String entityType;

    @Schema(description = "对象id")
    @TableField("entity_id")
    private Long entityId;

    @Schema(description = "所属模块")
    @TableField("module")
    private String module;

    @Schema(description = "菜单id")
    @TableField("menu_id")
    private Long menuId;

    @Schema(description = "权限授予类型（granted-有权；denied-无权；inheritable-有权继承）")
    @TableField("grant_type")
    private PrivilegeGrantTypeEnum grantType;

    @Schema(description = "权限范围（CURRENT_MENU-当前菜单；CURRENT_AND_SUB_MENUS-当前菜单及其下属菜单）")
    @TableField("privilege_scope")
    private String privilegeScope;

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
