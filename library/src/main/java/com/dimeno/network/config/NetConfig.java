package com.dimeno.network.config;

/**
 * NetConfig
 * Created by wangzhen on 2020/4/15.
 */
public class NetConfig {
    public String baseUrl;

    public NetConfig(Builder builder) {
        this.baseUrl = builder.baseUrl;
    }

    public static class Builder {
        String baseUrl;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public NetConfig build() {
            return new NetConfig(this);
        }
    }
}
