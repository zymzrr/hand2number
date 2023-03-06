package com.hs.trace.common;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 返回工具类
 *
 * @author lx
 */
public class Results {

    public static <T> Result<T> success() {
        return new Result<>(ResultCodeEnum.SUC, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCodeEnum.SUC, data);
    }

    public static <T> Result<T> error() {
        return new Result<>(ResultCodeEnum.COMMONERROR, null);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(ResultCodeEnum.COMMONERROR, msg, null);
    }

    public static <T> Result<T> error(ResultCodeEnum resultCodeEnum) {
        return new Result<>(resultCodeEnum, null);
    }

    public static <T> Result<T> errorNotShow(String msg) {
        Result result = error(msg);
//        result.setIsShowMessage(0);
        return result;
    }

    public static <T> Result<T> errorNotShow(ResultCodeEnum resultCodeEnum) {
        Result result = error(resultCodeEnum);
//        result.setIsShowMessage(0);
        return result;
    }

    public static <T> Result<T> result(Supplier<Result<T>> supplier) {
        return supplier.get();
    }

    public static <T> Result<T> result(T t, Function<T, Result<T>> function) {
        return function.apply(t);
    }

    public static Result<String> result(boolean ret, String errorMsg) {
        if (ret) {
            return new Result<>(ResultCodeEnum.SUC, null);
        } else {
            return new Result<>(ResultCodeEnum.COMMONERROR, errorMsg, null);
        }
    }
}
