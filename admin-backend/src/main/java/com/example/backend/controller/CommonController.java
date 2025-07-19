package com.example.backend.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.example.backend.common.Response.CommonReturnType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 无需登录即可访问的公用方法
 */
@CrossOrigin
@RestController
@RequestMapping("/common")
public class CommonController {

    @GetMapping("/getSnowId")
    public CommonReturnType getSnowId() {
        return CommonReturnType.success(IdWorker.getIdStr());
    }
}
