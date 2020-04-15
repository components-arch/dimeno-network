package com.dimeno.network.callback;

/**
 * ApiCallback
 * Created by wangzhen on 2020/4/15.
 */
public interface RequestCallback<ResultType> {
    void onStart();

    void onProgress(int progress);

    void onSuccess(ResultType data);

    void onError(int code, String message);

    void onCancel();

    void onComplete();
}
