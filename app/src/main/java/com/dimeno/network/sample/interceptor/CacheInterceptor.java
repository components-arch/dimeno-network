package com.dimeno.network.sample.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * CacheInterceptor
 * Created by wangzhen on 2020/9/5.
 */
public class CacheInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        // 设置响应的缓存时间为60秒，即设置Cache-Control头，并移除pragma消息头，因为pragma也是控制缓存的一个消息头属性
        return chain.proceed(chain.request()).newBuilder()
                .removeHeader("pragma")
                .header("Cache-Control", "max-age=60")
                .build();
    }
}
