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
                onError(ErrorType.UNKNOWN_ERROR, e.getMessage(), callback);
            }
        } else {
            onError(ErrorType.SERVER_FAILED, ErrorType.wrap(response.code()), callback);
        }
    }

    private static <EntityType> void handleBody(final Response response, final RequestCallback<EntityType> callback) throws IOException {
        if (response.body() != null) {
            String body = response.body().string();
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
                }
            }
            onSuccess(data, callback);
        } else {
            onError(ErrorType.RESPONSE_BODY_EMPTY, "empty response body", callback);
        }
    }

    public static <EntityType> void parseError(IOException e, RequestCallback<EntityType> callback) {
        int code;
        if (e instanceof UnknownHostException) {
            code = ErrorType.UNKNOWN_HOST;
        } else if (e instanceof SocketTimeoutException) {
            code = ErrorType.TIME_OUT;
            // 解决停留网络超时问题
            ClientLoader.getClient().connectionPool().evictAll();
        } else if (e instanceof ConnectException) {
            // 网络连接失败(eg:代理出问题)
            code = ErrorType.CONNECT_EXCEPTION;
        } else {
            code = ErrorType.UNKNOWN_ERROR;
        }
        onError(code, ErrorType.wrap(code), callback);
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

    private static void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
}
