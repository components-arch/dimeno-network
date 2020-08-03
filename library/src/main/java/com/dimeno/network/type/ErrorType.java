package com.dimeno.network.type;

/**
 * error type
 * Created by wangzhen on 2020/4/15.
 */
public class ErrorType {
    public static class Code {
        public static final int TIME_OUT = 0;
        public static final int CONNECT_EXCEPTION = 1;
        public static final int UNKNOWN_HOST = 2;
        public static final int UNKNOWN_ERROR = 3;
        public static final int EMPTY_BODY = 4;
        public static final int NULL_DATA = 5;
    }

    public static class Message {
        public static final String SYSTEM_BUSY = "系统繁忙，请稍后再试";
        public static final String UNKNOWN_HOST = "网络异常";
        public static final String TIME_OUT = "网络超时";
        public static final String CONNECT_EXCEPTION = "网络连接失败";
    }
}
