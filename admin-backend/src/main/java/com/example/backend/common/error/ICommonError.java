package com.example.backend.common.error;

public interface ICommonError {
    int getErrCode();

    String getErrMsg();

    ICommonError setErrMsg(String errMsg);
}
