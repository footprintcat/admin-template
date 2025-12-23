package com.example.backend.common.error;

public enum BusinessErrorCode implements ICommonError {
    // 10000 通用错误类型
    UNKNOWN_ERROR(10001, "未知错误"),
    PARAMETER_VALIDATION_ERROR(10002, "参数不合法"),
    NOT_SUPPORT(10003, "操作或方法不支持"),
    NOT_IMPLEMENT(10004, "方法未实现"),
    FAULT_ERROR(10005, "致命错误"),
    INCORRECT_DATA(10006, "数据不正确"),
    ILLEGAL_CALL(10007, "非法调用"),
    EXCESSIVE_DATA_VOLUME(10008, "数据量过大，请缩小查询范围后重试"),
    API_NOT_EXIST(10009, "请求的接口不存在"),

    // 20000 用户信息相关错误定义
    USER_NOT_EXIST(20001, "用户不存在"),
    USER_LOGIN_FAILED(20002, "用户账号或密码不正确"),
    USER_NOT_LOGIN(20003, "用户还未登录"),
    USER_TOKEN_EXPIRED(20004, "用户令牌过期"),
    USER_ALREADY_EXIST(20005, "用户已存在"),
    USER_NOT_ALLOWED_LOGIN(20006, "当前用户状态不允许登录"),
    USER_NOT_SELECT_IDENTITY(20007, "用户还未选择登入身份"),

    // 30000 权限相关错误定义
    OPERATION_NOT_ALLOWED(30001, "用户没有此操作的权限"),

    // 40000 OPEN 开放平台相关错误定义
    OPEN_AUTHENTICATION_FAILURE(40001, "用户身份验证失败"),

    // 50000 三方平台或设备调用异常
    THIRD_PARTY_SERVICE_EXCEPTION(50001, "三方服务异常"),

    // 占位
    PLACE_HOLDER(99999, "这是一个占位符错误");

    BusinessErrorCode(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private final int errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public ICommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
