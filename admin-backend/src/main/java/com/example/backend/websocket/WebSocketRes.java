package com.example.backend.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Data
@Slf4j
public class WebSocketRes {

    private List<WebSocketResData> data = new LinkedList<>();

    private WebSocketRes() {

    }

    public static WebSocketRes create() {
        return new WebSocketRes();
    }

    public WebSocketRes addData(WebSocketResData webSocketResData) {
        if (webSocketResData != null) {
            this.data.add(webSocketResData);
        }
        return this;
    }

    public WebSocketRes addData(LinkedList<WebSocketResData> webSocketResDataLinkedList) {
        if (webSocketResDataLinkedList != null) {
            this.data = webSocketResDataLinkedList;
        }
        return this;
    }

    public String toJSON() {
        // 使用 Jackson 序列化，保留 null 值
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("WebSocket 序列化失败", e);
            return "{}";
        }
    }
}
