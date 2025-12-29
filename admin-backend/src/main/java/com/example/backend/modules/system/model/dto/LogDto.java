package com.example.backend.modules.system.model.dto;

import lombok.Data;

@Data
public class LogDto {

    private String id;
    private Long createTimestamp;
    private String action;
    private String userId;
    private String ip;
    private String title;
    private String content;

}
