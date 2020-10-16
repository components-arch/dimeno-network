package com.dimeno.network.base;

import androidx.annotation.NonNull;

import com.dimeno.network.loading.LoadingPage;

import okhttp3.Call;

/**
 * network task callback
 * Created by wangzhen on 2020/4/15.
 */
public interface Task {
    void onSetupParams(Object... params);

    String getApi();

    Task put(String key, Object value);

    Task putFile(String key, String filePath);

    Task addHeader(String name, String value);

    Task setTag(Object tag);

    Task setLoadingPage(@NonNull LoadingPage page);

    Call exe(Object... params);

    Call retry();
}
