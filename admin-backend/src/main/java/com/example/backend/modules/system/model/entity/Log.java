package com.example.backend.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.backend.modules.system.enums.log.LogSourceEnum;
import com.example.backend.modules.system.enums.log.LogTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author coder-xiaomo
 * @since 2025-12-12
 */
@Getter
@Setter
@Schema(name = "Log", description = "系统日志表")
@TableName("system_log")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "日志来源（backend-后端日志；manage-管理端前端上报日志；app-移动端上报日志）")
    @TableField("source")
    private LogSourceEnum source;

    @Schema(description = "日志类型（见后端 LogTypeEnum 枚举类）")
    @TableField("type")
    private LogTypeEnum type;

    @Schema(description = "日志记录对象")
    @TableField("object_name")
    private String objectName;

    @Schema(description = "日志标题")
    @TableField("title")
    private String title;

    @Schema(description = "简单描述")
    @TableField("intro")
    private String intro;

    @Schema(description = "日志正文")
    @TableField("detail_id")
    private Long detailId;

    @Schema(description = "客户端IP地址")
    @TableField("client_ip")
    private String clientIp;

    @Schema(description = "租户id")
    @TableField("tenant_id")
    private Long tenantId;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
