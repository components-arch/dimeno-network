package com.dimeno.network;

import android.content.Context;

import com.dimeno.network.base.Common;
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
    private static OkHttpClient sHttpClient;

    /**
     * get okhttp client instance
     *
     * @return okhttp client
     */
    public static OkHttpClient getClient() {
        if (sHttpClient == null) {
            synchronized (ClientLoader.class) {
                if (sHttpClient == null) {
                    sHttpClient = config(newClient());
                }
            }
        }
        return sHttpClient;
    }

    /**
     * create a new okhttp client
     *
     * @return okhttp client
     */
    private static OkHttpClient newClient() {
        SSLSocketManager sslSocketManager = new SSLSocketManager();
        return new OkHttpClient.Builder()
                .addInterceptor(new ProgressInterceptor())
                .retryOnConnectionFailure(true)
                .sslSocketFactory(sslSocketManager.getSSLSocketFactory(), sslSocketManager.getX509TrustManager())
                .hostnameVerifier(sslSocketManager.getHostnameVerifier())
                .connectTimeout(Common.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Common.DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Common.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .cache(getCache())
                .build();
    }

    /**
     * apply external config to okhttp client
     *
     * @param client okhttp client
     * @return {@link NetConfig}
     */
    private static OkHttpClient config(OkHttpClient client) {
        NetConfig config = Network.sConfig;
        if (config != null) {
            OkHttpClient.Builder builder = client.newBuilder();
            if (config.interceptors != null && !config.interceptors.isEmpty()) {
                for (Interceptor interceptor : config.interceptors) {
                    builder.addInterceptor(interceptor);
                }
            }
            builder.retryOnConnectionFailure(config.retryOnConnectionFailure);
            if (config.connectTimeout > 0) {
                builder.connectTimeout(config.connectTimeout, TimeUnit.SECONDS);
            } else {
                builder.connectTimeout(Common.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
            }
            if (config.readTimeout > 0) {
                builder.readTimeout(config.readTimeout, TimeUnit.SECONDS);
            } else {
                builder.readTimeout(Common.DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
            }
            if (config.writeTimeout > 0) {
                builder.writeTimeout(config.writeTimeout, TimeUnit.SECONDS);
            } else {
                builder.writeTimeout(Common.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
            }
            client = builder.build();
        }
        return client;
    }

    /**
     * create cache dir
     *
     * @return cache
     */
    private static Cache getCache() {
        Context context = ContextProvider.sContext;
        if (context == null) {
            context = Network.sContext;
        }
        if (context == null) {
            throw new NullPointerException("Context is null, call Network.init(Context, NetConfig) to provide context.");
        }
        File httpCacheDirectory = new File(context.getExternalCacheDir(), "HttpCache");
        return new Cache(httpCacheDirectory, 10 * 1024 * 1024);
    }
}
