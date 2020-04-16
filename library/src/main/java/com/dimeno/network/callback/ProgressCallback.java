package com.dimeno.network.callback;

/**
 * progress callback
 * Created by wangzhen on 2020/4/16.
 */
public abstract class ProgressCallback<ResultType> implements RequestCallback<ResultType> {
    @Override
    public void onStart() {

    }

    @Override
    public void onSuccess(ResultType data) {
        
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
