package com.example.backend.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@Schema(name = "SystemConfig", description = "系统设置及临时信息存储表")
@TableName("system_config")
public class SystemConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "键")
    @TableField("config")
    private String config;

    @Schema(description = "值")
    @TableField("value")
    private String value;

    @Schema(description = "该配置属于哪个系统('data-process', 'backend-server', 'management', 'smart-screen')")
    @TableField("owner")
    private String owner;

    @Schema(description = "过期时间")
    @TableField("expire_timestamp")
    private Long expireTimestamp;

    @Schema(description = "展示名称")
    @TableField("config_name")
    private String configName;

    @Schema(description = "备注信息（方便配置，无实际用途）")
    @TableField("comment")
    private String comment;
}
