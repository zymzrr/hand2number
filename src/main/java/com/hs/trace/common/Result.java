package com.hs.trace.common;

import lombok.Data;

@Data
public class Result<T> {
    /**
     * 状态码
     */
    private int code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 是否全局展示错误消息：1-是，0-否
     */
//    private int isShowMessage = 1;

    /**
     * 数据
     */
    private T data;

    public Result() {

    }

    public Result(ResultCodeEnum code, T data) {
        this.code = code.getValue();
        this.msg = code.getMsg();
        this.data = data;
    }

    public Result(ResultCodeEnum code, String msg, T data) {
        this.code = code.getValue();
        this.msg = msg;
        this.data = data;
    }

//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public T getData() {
//        return data;
//    }
//
//    public void setData(T data) {
//        this.data = data;
//    }
//
//    public int getIsShowMessage() {
//        return isShowMessage;
//    }
//
//    public void setIsShowMessage(int isShowMessage) {
//        this.isShowMessage = isShowMessage;
//    }

    public boolean success(){
        return code == ResultCodeEnum.SUC.getValue();
    }
}
