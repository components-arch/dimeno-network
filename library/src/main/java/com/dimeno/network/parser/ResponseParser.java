package com.dimeno.network.parser;

import android.os.Handler;
import android.os.Looper;

import com.dimeno.network.ClientLoader;
import com.dimeno.network.callback.RequestCallback;
import com.dimeno.network.loading.LoadingPage;
import com.dimeno.network.type.ErrorType;
import com.dimeno.network.util.Generics;
import com.dimeno.network.util.JsonUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * response parser
 * Created by wangzhen on 2020/4/15.
 */
public final class ResponseParser {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private LoadingPage mLoadingPage;

    public static ResponseParser get() {
        return Inner.INSTANCE;
    }

    private static class Inner {
        static ResponseParser INSTANCE = new ResponseParser();
    }

    public ResponseParser loadingPage(LoadingPage page) {
        this.mLoadingPage = page;
        return this;
    }

    public <EntityType> void parseResponse(Response response, RequestCallback<EntityType> callback) {
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

    private <EntityType> void handleBody(final Response response, final RequestCallback<EntityType> callback) throws IOException {
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

    public <EntityType> void parseError(IOException e, RequestCallback<EntityType> callback) {
        if (e instanceof SocketTimeoutException) {
            onError(ErrorType.Code.TIME_OUT, ErrorType.Message.TIME_OUT, callback);
            // 解决停留网络超时问题
            ClientLoader.getClient().connectionPool().evictAll();
        } else if (e instanceof ConnectException) {
            // 网络连接失败(eg:代理出问题)
            onError(ErrorType.Code.CONNECT_EXCEPTION, ErrorType.Message.CONNECT_EXCEPTION, callback);
        } else if (e instanceof UnknownHostException) {
            onError(ErrorType.Code.UNKNOWN_HOST, ErrorType.Message.UNKNOWN_HOST, callback);
        } else if (e instanceof SocketException) {
            // 主动取消
            onCancel(callback);
        } else {
            onError(ErrorType.Code.UNKNOWN_ERROR, ErrorType.Message.SYSTEM_BUSY, callback);
        }
        onComplete(callback);
    }

    private <EntityType> void onSuccess(final EntityType data, final RequestCallback<EntityType> callback) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingPage != null) {
                    mLoadingPage.onSuccess();
                }
                if (callback != null) {
                    callback.onSuccess(data);
                }
            }
        });
    }

    private <EntityType> void onError(final int code, final String message, final RequestCallback<EntityType> callback) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingPage != null) {
                    mLoadingPage.onError();
                } else {
                    if (callback != null) {
                        callback.onError(code, message);
                    }
                }
            }
        });
    }

    private <EntityType> void onCancel(final RequestCallback<EntityType> callback) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingPage != null) {
                    mLoadingPage.onError();
                } else {
                    if (callback != null) {
                        callback.onCancel();
                    }
                }
            }
        });
    }

    private <EntityType> void onComplete(final RequestCallback<EntityType> callback) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onComplete();
                }
            }
        });
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
}
