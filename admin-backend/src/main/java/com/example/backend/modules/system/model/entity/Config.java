package com.example.backend.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.example.backend.modules.system.enums.config.ConfigScopeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统设置及临时信息存储表
 * </p>
 *
 * @author coder-xiaomo
 * @since 2023-05-23
 */
@Getter
@Setter
@Schema(name = "Config", description = "系统设置及临时信息存储表")
@TableName("system_config")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "该配置属于哪个系统('backend', 'management', 'app')")
    @TableField("scope")
    private ConfigScopeEnum scope;

    @Schema(description = "键")
    @TableField("config")
    private String config;

    @Schema(description = "值")
    @TableField("value")
    private String value;

    @Schema(description = "过期时间")
    @TableField("expire_time")
    private LocalDateTime expireTime;

    @Schema(description = "展示名称")
    @TableField("config_name")
    private String configName;

    @Schema(description = "备注信息（方便配置，无实际用途）")
    @TableField("comment")
    private String comment;

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

    @Schema(description = "版本号（乐观锁）")
    @TableField("version")
    @Version
    private Long version;
}
