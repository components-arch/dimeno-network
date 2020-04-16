package com.dimeno.network;

import com.dimeno.network.interceptor.ProgressInterceptor;
import com.dimeno.network.manager.SSLSocketManager;
import com.dimeno.network.provider.ContextProvider;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * ClientLoader
 * Created by wangzhen on 2020/4/15.
 */
public class ClientLoader {
    public static OkHttpClient getClient() {
        SSLSocketManager sslSocketManager = new SSLSocketManager();
        return new OkHttpClient.Builder()
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

    private static Cache getCache() {
        // /data/user/0/{package}/cache
        File httpCacheDirectory = new File(ContextProvider.sContext.getCacheDir(), "HttpCache");
        return new Cache(httpCacheDirectory, 10 * 1024 * 1024);
    }
}
