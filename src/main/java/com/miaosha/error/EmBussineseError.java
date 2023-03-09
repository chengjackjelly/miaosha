package com.miaosha.error;

public enum EmBussineseError implements CommonError{


    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKOWN_ERROR(10002,"未知错误"),


    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_ERROR(20002,"手机号或密码错误"),
    USER_NOT_LOGIN(20003,"用户未登录"),

    ITEM_NOT_EXIST(30001,"商品不存在"),
    ITEM_ERROR(30002,"商品字段缺失"),
    ITEM_STOCK_LIMIT(30003,"商品库存不足");


    ;

    private int errCode;
    private String errMsg;
    private EmBussineseError(int errCode,String errMsg){
        this.errCode=errCode;
        this.errMsg=errMsg;
    }


    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public void setErrMsg(String errMsg) {
        this.errMsg=errMsg;
    }
}
