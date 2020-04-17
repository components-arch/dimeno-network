package com.dimeno.network.callback;

/**
 * LoadingCallback
 * Created by wangzhen on 2020/4/15.
 */
public abstract class LoadingCallback<ResultType> implements RequestCallback<ResultType> {

    @Override
    public void onStart() {

    }

    @Override
    public void onError(int code, String message) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onComplete() {

    }
}
