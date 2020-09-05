package com.dimeno.network.config;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * net config
 * Created by wangzhen on 2020/4/15.
 */
public class NetConfig {
    public String baseUrl;
    public List<Interceptor> netInterceptors;
    public List<Interceptor> interceptors;
    public boolean retryOnConnectionFailure;
    public long connectTimeout;
    public long readTimeout;
    public long writeTimeout;

    private NetConfig(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.netInterceptors = builder.netInterceptors;
        this.interceptors = builder.interceptors;
        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
    }

    public static class Builder {
        String baseUrl;
        List<Interceptor> netInterceptors;
        List<Interceptor> interceptors;
        boolean retryOnConnectionFailure = true;
        long connectTimeout;
        long readTimeout;
        long writeTimeout;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder netInterceptor(Interceptor interceptor) {
            if (this.netInterceptors == null) {
                this.netInterceptors = new ArrayList<>();
            }
            this.netInterceptors.add(interceptor);
            return this;
        }

        public Builder interceptor(Interceptor interceptor) {
            if (this.interceptors == null) {
                this.interceptors = new ArrayList<>();
            }
            this.interceptors.add(interceptor);
            return this;
        }

        public Builder retryOnConnectionFailure(boolean retry) {
            this.retryOnConnectionFailure = retry;
            return this;
        }

        public Builder connectTimeout(long connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder writeTimeout(long writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public NetConfig build() {
            return new NetConfig(this);
        }
    }
}
