package com.dimeno.network;

import com.dimeno.network.config.NetConfig;

/**
 * Network
 * Created by wangzhen on 2020/4/15.
 */
public final class Network {
    public static NetConfig sConfig;

    public static void init(NetConfig config) {
        sConfig = config;
    }
}
