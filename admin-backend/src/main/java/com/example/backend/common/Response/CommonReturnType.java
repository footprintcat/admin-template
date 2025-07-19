package com.example.backend.common.Response;

import lombok.Data;

@Data
public class CommonReturnType {
    // 表明对应请求的返回处理结果 "success" 或 "fail"
    private String status;

    // status 是否是 SUCCESS
    private Boolean isSuccess;

    // 若 status == "success" 则data内返回前端需要的JSON数据
    // 若 status == "fail" 则data内使用通用的错误码格式
    private Object data;

    // 失败时的错误消息
    private String message;

    // 定义一个通用的创建方法
    private static CommonReturnType create(Object result, String msg, CommonReturnTypeStatus status) {
        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setMessage(msg);
        type.setStatus(status.toString());
        type.setIsSuccess(status == CommonReturnTypeStatus.SUCCESS);
        return type;
    }

    public static CommonReturnType success(Object result, String message) {
        return CommonReturnType.create(result, message, CommonReturnTypeStatus.SUCCESS);
    }

    public static CommonReturnType success(Object result) {
        return CommonReturnType.create(result, null, CommonReturnTypeStatus.SUCCESS);
    }

    public static CommonReturnType success() {
        return CommonReturnType.create(null, null, CommonReturnTypeStatus.SUCCESS);
    }

    public static CommonReturnType error(Object result, String message) {
        return CommonReturnType.create(result, message, CommonReturnTypeStatus.FAILED);
    }

    public static CommonReturnType error(String message) {
        return CommonReturnType.create(null, message, CommonReturnTypeStatus.FAILED);
    }

    public static CommonReturnType error() {
        return CommonReturnType.create(null, "系统内部错误", CommonReturnTypeStatus.FAILED);
    }
}