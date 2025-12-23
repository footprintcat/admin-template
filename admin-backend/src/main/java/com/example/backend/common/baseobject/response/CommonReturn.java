package com.example.backend.common.baseobject.response;

import com.example.backend.common.error.BusinessErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@Schema(description = "通用返回对象")
public class CommonReturn {

    private CommonReturn() {
    }

    // 表明对应请求的返回处理结果 "success" 或 "fail"
    @NotNull
    @Schema(description = "业务处理状态")
    private String status;

    // status 是否是 SUCCESS
    @NotNull
    @Schema(description = "业务处理是否成功")
    private Boolean isSuccess;

    // 若 status == "success" 则data内返回前端需要的JSON数据
    // 若 status == "fail" 则data内使用通用的错误码格式
    @Nullable
    @Schema(description = "返回结果")
    private Object data;

    // 失败时的错误消息
    @Nullable
    @Schema(description = "失败时的错误消息")
    private String message;

    // 失败时的错误详情
    @Nullable
    @Schema(description = "失败时的错误code")
    private Integer errCode;

    public static CommonReturn success(@Nullable Object result, @Nullable String message) {
        CommonReturn type = new CommonReturn();
        type.setData(result);
        type.setMessage(message);
        type.setStatus(CommonReturnStatus.SUCCESS.toString());
        type.setIsSuccess(true);
        return type;
    }

    public static CommonReturn success(@Nullable Object result) {
        return CommonReturn.success(result, null);
    }

    public static CommonReturn success() {
        return CommonReturn.success(null, null);
    }

    public static CommonReturn errorByCode(@Nullable Integer errCode, @Nullable String errMsg) {
        CommonReturn type = new CommonReturn();
        type.setErrCode( errCode);
        type.setMessage(errMsg);
        type.setStatus(CommonReturnStatus.FAILED.toString());
        type.setIsSuccess(false);
        return type;
    }

    public static CommonReturn error(@Nullable BusinessErrorCode businessErrorCode, @Nullable String errMsg) {
        if (businessErrorCode == null) {
            return CommonReturn.errorByCode(null, errMsg);
        }
        return CommonReturn.errorByCode(businessErrorCode.getErrCode(), errMsg);
    }

    public static CommonReturn error(@Nullable BusinessErrorCode businessErrorCode) {
        if (businessErrorCode == null) {
            return CommonReturn.errorByCode(null, null);
        }
        return CommonReturn.errorByCode(businessErrorCode.getErrCode(), businessErrorCode.getErrMsg());
    }

    public static CommonReturn error(@Nullable String errMsg) {
        return CommonReturn.errorByCode(null, errMsg);
    }

    public static CommonReturn error() {
        return CommonReturn.errorByCode(null, "系统内部错误");
    }
}
