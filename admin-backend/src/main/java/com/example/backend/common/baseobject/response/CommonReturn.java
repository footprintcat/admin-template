package com.example.backend.common.baseobject.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@Schema(description = "通用返回对象")
public class CommonReturn {

    private CommonReturn() {
    }

    // 表明对应请求的返回处理结果 "success" 或 "fail"
    @Schema(description = "业务处理状态")
    private String status;

    // status 是否是 SUCCESS
    @Schema(description = "业务处理是否成功")
    private Boolean isSuccess;

    // 若 status == "success" 则data内返回前端需要的JSON数据
    // 若 status == "fail" 则data内使用通用的错误码格式
    @Schema(description = "返回结果")
    private Object data;

    // 失败时的错误消息
    @Schema(description = "失败时的错误消息")
    private String message;

    // 定义一个通用的创建方法
    private static CommonReturn create(Object result, String msg, CommonReturnStatus status) {
        CommonReturn type = new CommonReturn();
        type.setData(result);
        type.setMessage(msg);
        type.setStatus(status.toString());
        type.setIsSuccess(status == CommonReturnStatus.SUCCESS);
        return type;
    }

    public static CommonReturn success(@Nullable Object result, @Nullable String message) {
        return CommonReturn.create(result, message, CommonReturnStatus.SUCCESS);
    }

    public static CommonReturn success(@Nullable Object result) {
        return CommonReturn.create(result, null, CommonReturnStatus.SUCCESS);
    }

    public static CommonReturn success() {
        return CommonReturn.create(null, null, CommonReturnStatus.SUCCESS);
    }

    public static CommonReturn error(@Nullable Object result, @Nullable String message) {
        return CommonReturn.create(result, message, CommonReturnStatus.FAILED);
    }

    public static CommonReturn error(@Nullable String message) {
        return CommonReturn.create(null, message, CommonReturnStatus.FAILED);
    }

    public static CommonReturn error() {
        return CommonReturn.create(null, "系统内部错误", CommonReturnStatus.FAILED);
    }
}
