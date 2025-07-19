package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author coder-xiaomo
 * @since 2024-01-11
 */
@Getter
@Setter
@TableName("system_log")
@Schema(name = "SystemLog", description = "")
public class SystemLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "雪花id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("log_time")
    private Date logTime;

    @Schema(description = "会话唯一id")
    @TableField("session_id")
    private String sessionId;

    @Schema(description = "用户id")
    @TableField("user_id")
    private String userId;

    @Schema(description = "角色id")
    @TableField("role_id")
    private String roleId;

    @TableField("login_time")
    private Date loginTime;

    @Schema(description = "行为")
    @TableField("action")
    private String action;

    @Schema(description = "Api地址")
    @TableField("api_path")
    private String apiPath;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    @Schema(description = "用户浏览器UA")
    @TableField("user_agent")
    private String userAgent;
}
