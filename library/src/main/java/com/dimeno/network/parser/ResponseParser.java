package com.dimeno.network.parser;

import android.os.Handler;
import android.os.Looper;

import com.dimeno.network.ClientLoader;
import com.dimeno.network.callback.RequestCallback;
import com.dimeno.network.type.ErrorType;
import com.dimeno.network.util.Generics;
import com.dimeno.network.util.JsonUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * ResponseParser
 * Created by wangzhen on 2020/4/15.
 */
public final class ResponseParser {
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static <EntityType> void parseResponse(Response response, RequestCallback<EntityType> callback) {
        if (response.code() == 200) {
            try {
                handleBody(response, callback);
            } catch (Exception e) {
                onError(ErrorType.Code.UNKNOWN_ERROR, e.getMessage(), callback);
            }
        } else {
            onError(response.code(), response.message(), callback);
        }
        onComplete(callback);
    }

    private static <EntityType> void handleBody(final Response response, final RequestCallback<EntityType> callback) throws IOException {
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String body = responseBody.string();
            EntityType data = null;
            if (callback != null) {
                Type typeOf = Generics.getGenericType(callback.getClass(), RequestCallback.class);
                if (typeOf == null) {
                    data = null;
                } else if (typeOf == Void.class) {
                    data = null;
                } else if (typeOf == String.class) {
                    data = (EntityType) body;
                } else {
                    data = JsonUtils.parseObject(body, typeOf);
                    if (data == null) {
                        onError(ErrorType.Code.NULL_DATA, "null data", callback);
                        return;
                    }
                }
            }
            onSuccess(data, callback);
        } else {
            onError(ErrorType.Code.EMPTY_BODY, "empty response body", callback);
        }
    }

    public static <EntityType> void parseError(IOException e, RequestCallback<EntityType> callback) {
        int code;
        String message;
        if (e instanceof SocketTimeoutException) {
            code = ErrorType.Code.TIME_OUT;
            message = ErrorType.Message.TIME_OUT;
            // 解决停留网络超时问题
            ClientLoader.getClient().connectionPool().evictAll();
        } else if (e instanceof ConnectException) {
            // 网络连接失败(eg:代理出问题)
            code = ErrorType.Code.CONNECT_EXCEPTION;
            message = ErrorType.Message.CONNECT_EXCEPTION;
        } else if (e instanceof UnknownHostException) {
            code = ErrorType.Code.UNKNOWN_HOST;
            message = ErrorType.Message.UNKNOWN_HOST;
        } else {
            code = ErrorType.Code.UNKNOWN_ERROR;
            message = ErrorType.Message.SYSTEM_BUSY;
        }
        onError(code, message, callback);
        onComplete(callback);
    }

    private static <EntityType> void onSuccess(final EntityType data, final RequestCallback<EntityType> callback) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(data);
                }
            }
        });
    }

    private static <EntityType> void onError(final int code, final String message, final RequestCallback<EntityType> callback) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onError(code, message);
                }
            }
        });
    }

    private static <EntityType> void onComplete(final RequestCallback<EntityType> callback) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onComplete();
                }
            }
        });
    }

    private static void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
}
