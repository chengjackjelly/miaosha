package com.miaosha.error;

public interface CommonError {
    int getErrCode();
    String getErrMsg();
    void setErrMsg(String errMsg);
}
