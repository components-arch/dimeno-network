package com.dimeno.network.parser;

import android.os.Handler;
import android.os.Looper;

import com.dimeno.network.ClientLoader;
import com.dimeno.network.Network;
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

    private ResponseParser() {
    }

    public static ResponseParser newInstance() {
        return new ResponseParser();
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
                onError(ErrorType.Code.UNKNOWN_ERROR, convert(ErrorType.Message.SYSTEM_BUSY, e.getMessage()), callback);
            }
        } else {
            onError(response.code(), convert(ErrorType.Message.SYSTEM_BUSY, response.message()), callback);
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
                        onError(ErrorType.Code.NULL_DATA, convert(ErrorType.Message.SYSTEM_BUSY, "data is null"), callback);
                        return;
                    }
                }
            }
            onSuccess(data, callback);
        } else {
            onError(ErrorType.Code.EMPTY_BODY, convert(ErrorType.Message.SYSTEM_BUSY, "empty response body"), callback);
        }
    }

    public <EntityType> void parseError(IOException e, RequestCallback<EntityType> callback) {
        if (e instanceof SocketTimeoutException) {
            onError(ErrorType.Code.TIME_OUT, convert(ErrorType.Message.TIME_OUT, e.getMessage()), callback);
            // 解决停留网络超时问题
            ClientLoader.getClient().connectionPool().evictAll();
        } else if (e instanceof ConnectException) {
            // 网络连接失败(eg:代理出问题)
            onError(ErrorType.Code.CONNECT_EXCEPTION, convert(ErrorType.Message.CONNECT_EXCEPTION, e.getMessage()), callback);
        } else if (e instanceof UnknownHostException) {
            onError(ErrorType.Code.UNKNOWN_HOST, convert(ErrorType.Message.UNKNOWN_HOST, e.getMessage()), callback);
        } else if (e instanceof SocketException) {
            // 主动取消
            onCancel(callback);
        } else {
            onError(ErrorType.Code.UNKNOWN_ERROR, convert(ErrorType.Message.SYSTEM_BUSY, e.getMessage()), callback);
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

    private String convert(String friendlyMessage, String exceptionMessage) {
        if (Network.sConfig != null && Network.sConfig.debug) {
            return exceptionMessage;
        }
        return friendlyMessage;
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
}
