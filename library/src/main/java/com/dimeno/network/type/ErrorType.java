package com.dimeno.network.type;

/**
 * error type
 * Created by wangzhen on 2020/4/15.
 */
public class ErrorType {

    private static final String SYSTEM_BUSY = "系统繁忙，请稍后再试";

    public static final int TIME_OUT = 0;

    public static final int CONNECT_EXCEPTION = 1;

    public static final int UNKNOWN_HOST = 2;

    public static final int UNKNOWN_ERROR = 3;

    public static final int SERVER_FAILED = 4;

    public static final int RESPONSE_BODY_EMPTY = 5;

    public static String wrap(int code) {
        String message;
        switch (code) {
            case TIME_OUT:
                message = "超时";
                break;
            case CONNECT_EXCEPTION:
                message = "网络连接失败";
                break;
            case UNKNOWN_HOST:
                message = "网络异常";
                break;
            default:
                message = SYSTEM_BUSY;
                break;
        }
        return message;
    }
}
