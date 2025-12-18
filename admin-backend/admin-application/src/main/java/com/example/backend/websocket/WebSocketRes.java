package com.example.backend.websocket;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
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
        JSONObject jsonObject = new JSONObject(1);
        jsonObject.put("data", this.data);
        // 2024.06.18 bugfix: 大屏 websocket Map value为null时丢失字段问题修复
        String s = jsonObject.toString(JSONWriter.Feature.WriteMapNullValue);
        return s;
    }
}
