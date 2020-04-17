package com.dimeno.network.sample.interceptor;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * TestInterceptor
 * Created by wangzhen on 2020/4/17.
 */
public class TestInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Log.e("TAG", "-> TestInterceptor " + request.url().toString());
        return chain.proceed(request);
    }
}
