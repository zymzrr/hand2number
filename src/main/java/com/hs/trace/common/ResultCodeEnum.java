package com.hs.trace.common;

/**
 * 结果返回
 */
public enum ResultCodeEnum {

    /**
     * 基本结果码
     */
    SUC(0, "成功"),
    COMMONERROR(1, "通用错误"),
    NOT_LOGIN(2, "未登录或登陆已过期"),

    ;
    /**
     * 返回码
     */
    private int value;
    /**
     * 描述信息
     */
    private String msg;

    ResultCodeEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public int getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }

    public static ResultCodeEnum getEnum(int code) {
        ResultCodeEnum[] values = values();
        for (ResultCodeEnum value : values) {
            if (value.value == code) {
                return value;
            }
        }
        return null;
    }
}
