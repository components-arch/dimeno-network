package com.dimeno.network.interceptor;

import com.dimeno.network.callback.ProgressCallback;
import com.dimeno.network.progress.ProgressRequestBody;
import com.dimeno.network.progress.ProgressResponseBody;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * progress interceptor
 * Created by wangzhen on 2020/4/16.
 */
public class ProgressInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        if (request.tag() instanceof ProgressCallback) {
            ProgressCallback callback = (ProgressCallback) request.tag();
            if (request.body() != null) {
                //wrap request body
                request = request.newBuilder().post(new ProgressRequestBody(request.body(), callback)).build();
            }
            Response response = chain.proceed(request);
            if (response.body() != null) {
                //wrap response body
                response = response.newBuilder().body(new ProgressResponseBody(response.body(), callback)).build();
            }
            return response;
        }
        return chain.proceed(request);
    }
}
