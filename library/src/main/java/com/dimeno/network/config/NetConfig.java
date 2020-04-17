package com.dimeno.network.config;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * NetConfig
 * Created by wangzhen on 2020/4/15.
 */
public class NetConfig {
    public String baseUrl;
    public List<Interceptor> interceptors;

    private NetConfig(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.interceptors = builder.interceptors;
    }

    public static class Builder {
        String baseUrl;
        List<Interceptor> interceptors;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder interceptor(Interceptor interceptor) {
            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }
            interceptors.add(interceptor);
            return this;
        }

        public NetConfig build() {
            return new NetConfig(this);
        }
    }
}
