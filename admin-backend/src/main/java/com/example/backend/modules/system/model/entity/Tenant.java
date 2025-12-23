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
 * 系统租户表
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-14
 */
@Getter
@Setter
@Schema(name = "Tenant", description = "系统租户表")
@TableName("system_tenant")
public class Tenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "父租户id")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "租户层级")
    @TableField("`level`")
    private Integer level;

    @Schema(description = "租户名称")
    @TableField("tenant_name")
    private String tenantName;

    @Schema(description = "租户简介")
    @TableField("tenant_intro")
    private String tenantIntro;

    @Schema(description = "租户状态：normal-正常（可用）, locked-锁定（禁用）, disabled-停用, expired-过期")
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
