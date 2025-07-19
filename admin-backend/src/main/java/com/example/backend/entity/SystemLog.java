package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

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

    @Schema(description = "事件产生时间")
    @TableField(value = "log_time", insertStrategy = FieldStrategy.NOT_NULL)
    private Date logTime;

    @Schema(description = "会话唯一id")
    @TableField("session_id")
    private String sessionId;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "角色id")
    @TableField("role_id")
    private String roleId;

    @TableField("login_time")
    private Date loginTime;

    @Schema(description = "行为：登录、发短信、导出记录、删除、修改、触发预警等")
    @TableField("action")
    private String action;

    @Schema(description = "事件标题（以登录为例）：	管理员用户登录系统成功；	用户尝试登录失败；	……")
    @TableField("title")
    private String title;

    @Schema(description = "事件内容")
    @TableField("content")
    private String content;

    /**
     * 参考格式
     * {
     *     "table": "数据表",
     *     "field": "列名",
     *     "old": "旧值",
     *     "new": "新值",
     *     ....
     * }
     */
    @Schema(description = "数据变化前后的值（隐藏，不显示给用户）")
    @TableField("detail")
    private String detail;

    @Schema(description = "Api地址")
    @TableField("api_path")
    private String apiPath;

    @Schema(description = "登录ip")
    @TableField("ip")
    private String ip;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    @Schema(description = "用户浏览器UA")
    @TableField("user_agent")
    private String userAgent;
}
