package com.example.backend.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.example.backend.modules.system.enums.user.UserStatusEnum;
import com.example.backend.modules.system.enums.user.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-12
 */
@Getter
@Setter
@Schema(name = "User", description = "系统用户表")
@TableName("system_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "用户昵称")
    @TableField("nickname")
    private String nickname;

    @Schema(description = "用户类型：super_admin-超级管理员；member-普通用户")
    @TableField("type")
    private UserTypeEnum type;

    @Schema(description = "用户状态：normal-正常（可用）, locked-锁定（禁用）, disabled-停用, expired-过期")
    @TableField("status")
    private UserStatusEnum status;

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
