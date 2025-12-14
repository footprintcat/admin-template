package com.example.backend.common.error;

/**
 * 包装器业务异常类实现
 */
public class BusinessException extends Exception implements ICommonError {

    private final ICommonError commonError;

    public BusinessException(ICommonError commonError) {
        super(commonError.getErrMsg());
        this.commonError = commonError;
    }

    // 接收自定义 errMsg 的方式构造业务异常
    public BusinessException(ICommonError commonError, String errMsg) {
        super(errMsg);
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public ICommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
