package com.dimeno.network;

import com.dimeno.network.config.NetConfig;
import com.dimeno.network.interceptor.ProgressInterceptor;
import com.dimeno.network.manager.SSLSocketManager;
import com.dimeno.network.provider.ContextProvider;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * client loader
 * Created by wangzhen on 2020/4/15.
 */
public final class ClientLoader {
    /**
     * create an okhttp client
     *
     * @return okhttp client
     */
    public static OkHttpClient getClient() {
        SSLSocketManager sslSocketManager = new SSLSocketManager();
        return wrap(new OkHttpClient.Builder())
                .addInterceptor(new ProgressInterceptor())
                .retryOnConnectionFailure(true)
                .sslSocketFactory(sslSocketManager.getSSLSocketFactory(), sslSocketManager.getX509TrustManager())
                .hostnameVerifier(sslSocketManager.getHostnameVerifier())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .cache(getCache())
                .build();
    }

    /**
     * wrapper OkHttpClient.Builder with external config
     *
     * @param builder OkHttpClient.Builder
     * @return {@link NetConfig}
     */
    private static OkHttpClient.Builder wrap(OkHttpClient.Builder builder) {
        NetConfig config = Network.sConfig;
        if (config != null) {
            if (config.interceptors != null && !config.interceptors.isEmpty()) {
                for (Interceptor interceptor : config.interceptors) {
                    builder.addInterceptor(interceptor);
                }
            }
        }
        return builder;
    }

    /**
     * create cache dir
     *
     * @return cache
     */
    private static Cache getCache() {
        File httpCacheDirectory = new File(ContextProvider.sContext.getCacheDir(), "HttpCache");
        return new Cache(httpCacheDirectory, 10 * 1024 * 1024);
    }

}
