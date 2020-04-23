package com.dimeno.network.base;

import android.text.TextUtils;
import android.view.View;

import com.dimeno.network.ClientLoader;
import com.dimeno.network.Network;
import com.dimeno.network.callback.ProgressCallback;
import com.dimeno.network.callback.RequestCallback;
import com.dimeno.network.manager.CallManager;
import com.dimeno.network.manager.LifecycleManager;
import com.dimeno.network.parser.ResponseParser;
import com.dimeno.network.type.RequestType;
import com.dimeno.network.util.ParamsBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * base task
 * Created by wangzhen on 2020/4/15.
 */
public abstract class BaseTask<EntityType> implements Task, Callback {
    private RequestCallback<EntityType> mCallback;
    private RequestType mRequestType;
    private Object mParams;

    private Map<String, Object> mParamsMap;
    private Map<String, String> mFilesMap;
    private Map<String, Set<String>> mHeaders;
    private Object mTag;

    public BaseTask(RequestCallback<EntityType> callback, RequestType type) {
        this.mCallback = callback;
        this.mRequestType = type;
    }

    @Override
    public Call exe(Object... params) {
        this.mParams = params;
        onSetupParams(params);
        return doTask();
    }

    @Override
    public Call exe(Collection collection) {
        if (collection == null) {
            collection = Collections.EMPTY_LIST;
        }
        return exe(collection.toArray(new Object[0]));
    }

    @Override
    public Call exe(Map<String, Object> paramsMap, Map<String, String> filesMap) {
        if (paramsMap == null) {
            paramsMap = new HashMap<>();
        }
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            if (mParamsMap == null) {
                mParamsMap = new HashMap<>();
            }
            mParamsMap.put(entry.getKey(), entry.getValue());
        }
        if (filesMap == null) {
            filesMap = new HashMap<>();
        }
        for (Map.Entry<String, String> entry : filesMap.entrySet()) {
            if (mFilesMap == null) {
                mFilesMap = new HashMap<>();
            }
            mFilesMap.put(entry.getKey(), entry.getValue());
        }
        return doTask();
    }

    private Call doTask() {
        String url = transformUrl();
        Request.Builder builder = new Request.Builder();
        switch (mRequestType) {
            case GET:
                builder.url(ParamsBuilder.buildGet(mParamsMap, url));
                break;
            case POST_JSON:
                builder.url(url).post(ParamsBuilder.buildPostJson(mParamsMap));
                break;
            case POST_FORM:
                builder.url(url).post(ParamsBuilder.buildPostForm(mParamsMap));
                break;
            case UPLOAD:
                builder.url(url).post(ParamsBuilder.buildUpload(mParamsMap, mFilesMap));
                break;
        }
        ParamsBuilder.buildHeaders(builder, mHeaders);

        if (mCallback instanceof ProgressCallback) {
            builder.tag(mCallback);
        }

        if (mTag instanceof View) {
            LifecycleManager.registerView((View) mTag);
        }

        if (mCallback != null) {
            mCallback.onStart();
        }

        Call call = ClientLoader.getClient().newCall(builder.build());
        CallManager.get().add(mTag, call);
        call.enqueue(this);
        return call;
    }

    private String transformUrl() {
        String api = getApi();
        if (!TextUtils.isEmpty(api) && api.startsWith("/")) {
            if (Network.sConfig != null && !TextUtils.isEmpty(Network.sConfig.baseUrl)) {
                return Network.sConfig.baseUrl + api;
            }
        }
        return api;
    }

    @Override
    public Call retry() {
        return exe(mParams);
    }

    @Override
    public Task put(String key, Object value) {
        if (mParamsMap == null) {
            mParamsMap = new HashMap<>();
        }
        mParamsMap.put(key, value);
        return this;
    }

    @Override
    public Task putFile(String key, String filePath) {
        if (mFilesMap == null) {
            mFilesMap = new HashMap<>();
        }
        mFilesMap.put(key, filePath);
        return this;
    }

    @Override
    public Task addHeader(String name, String value) {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }
        Set<String> values = mHeaders.get(name);
        if (values == null) {
            values = new HashSet<>();
            mHeaders.put(name, values);
        }
        values.add(value);
        return this;
    }

    @Override
    public Task setTag(Object tag) {
        this.mTag = tag;
        return this;
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        CallManager.get().removeCall(mTag, call);
        ResponseParser.parseResponse(response, mCallback);
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        CallManager.get().removeCall(mTag, call);
        ResponseParser.parseError(e, mCallback);
    }
}
