package com.dimeno.network.base;

import okhttp3.Call;

/**
 * Task
 * Created by wangzhen on 2020/4/15.
 */
public interface Task {
    Call exe(Object... params);

    Call retry();

    void onSetupParams(Object... params);

    Task setTag(Object tag);

    String getApi();

    Task put(String key, Object value);

    Task putFile(String key, String filePath);

    Task addHeader(String name, String value);
}
