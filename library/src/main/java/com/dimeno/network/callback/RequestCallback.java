package com.dimeno.network.callback;

import androidx.annotation.MainThread;

/**
 * base callback
 * Created by wangzhen on 2020/4/15.
 */
public interface RequestCallback<ResultType> {
    @MainThread
    void onStart();

    @MainThread
    void onSuccess(ResultType data);

    /**
     * @param code    code
     * @param message message
     * @see com.dimeno.network.type.ErrorType
     */
    @MainThread
    void onError(int code, String message);

    @MainThread
    void onCancel();

    @MainThread
    void onComplete();
}
