package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统租户表
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-12
 */
@Getter
@Setter
@Schema(name = "SystemTenant", description = "系统租户表")
@TableName("system_tenant")
public class SystemTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "父租户id")
    @TableField("parent_tenant_id")
    private Long parentTenantId;

    @Schema(description = "租户层级")
    @TableField("`level`")
    private Integer level;

    @Schema(description = "租户名称")
    @TableField("tenant_name")
    private String tenantName;

    @Schema(description = "租户简介")
    @TableField("tenant_intro")
    private String tenantIntro;

    @Schema(description = "租户状态：NORMAL-正常（可用）, LOCKED-锁定（禁用）, DISABLED-停用, EXPIRED-过期")
    @TableField("status")
    private String status;

    @Schema(description = "创建人")
    @TableField("create_by")
    private Long createBy;

    @Schema(description = "更新人")
    @TableField("update_by")
    private Long updateBy;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除")
    @TableField("is_delete")
    @TableLogic
    private Integer isDelete;

    @Schema(description = "版本号（乐观锁）")
    @TableField("version")
    private Long version;
}
