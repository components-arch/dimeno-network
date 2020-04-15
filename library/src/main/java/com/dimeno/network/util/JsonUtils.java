package com.dimeno.network.util;

import com.google.gson.Gson;

/**
 * JsonUtils
 * Created by wangzhen on 2020/4/15.
 */
public class JsonUtils {
    static Gson gson = new Gson();

    public static String toJsonString(Object object) {
        if (object != null) {
            return gson.toJson(object);
        } else {
            return "{}";
        }
    }
}
