package com.dimeno.network.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * JsonUtils
 * Created by wangzhen on 2020/4/15.
 */
public class JsonUtils {
    private static Gson gson = new Gson();

    public static <T> T parseObject(String jsonString, Type typeOfT) {
        return gson.fromJson(jsonString, typeOfT);
    }

    public static String toJsonString(Object object) {
        if (object != null) {
            return gson.toJson(object);
        } else {
            return "{}";
        }
    }
}
