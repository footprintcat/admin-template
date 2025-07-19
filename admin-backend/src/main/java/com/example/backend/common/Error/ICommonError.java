package com.example.backend.common.Error;

public interface ICommonError {
    int getErrCode();

    String getErrMsg();

    ICommonError setErrMsg(String errMsg);
}