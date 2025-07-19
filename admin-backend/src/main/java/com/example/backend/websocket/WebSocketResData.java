package com.example.backend.websocket;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WebSocketResData {

    private String key;

    private Boolean isSuccess = true;

    /**
     * 查询正常返回结果
     */
    // 数据
    private Object data;

    // 数据中最新一条数据创建时间戳
    private Long updateTimestamp;

    // 当前服务器时间戳
    private Long currentTimestamp;

    // 是否前端定时更新
    private Boolean isAutoUpdate = true;

    // 下一次前端应在何时更新（时间戳） / 如果不需要更新则传null
    private Long nextPollingFrontendTimestamp;

    // 下次增量拉取时前端带回的Flag / 如果该字段不支持增量更新，则返回空字符串
    private String pollingFlag;

    /**
     * 查询失败返回结果
     */
    private String errMsg;
}
