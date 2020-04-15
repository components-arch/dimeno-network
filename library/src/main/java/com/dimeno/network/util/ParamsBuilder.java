package com.dimeno.network.util;

import android.text.TextUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * ParamsBuilder
 * Created by wangzhen on 2020/4/15.
 */
public class ParamsBuilder {
    /**
     * build upload params
     *
     * @param paramsMap params map
     * @param filesMap  files map
     * @return request body
     */
    public static RequestBody buildUpload(Map<String, Object> paramsMap,
                                          Map<String, String> filesMap) {
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                if (TextUtils.isEmpty(entry.getKey()))
                    continue;

                Object value = entry.getValue();
                multipartBodyBuilder.addFormDataPart(entry.getKey(), value == null ? "" :
                        value.toString());
            }
        }

        if (filesMap != null && !filesMap.isEmpty()) {
            for (Map.Entry<String, String> entry : filesMap.entrySet()) {
                if (TextUtils.isEmpty(entry.getKey()))
                    continue;

                String value = entry.getValue();
                if (!TextUtils.isEmpty(value)) {
                    File file = new File(value);
                    if (file.exists()) {
                        final String fileName = file.getName();
                        multipartBodyBuilder.addFormDataPart(
                                entry.getKey(),
                                fileName,
                                RequestBody.create(createMediaType(fileName), file));
                    }
                }
            }
        }
        return multipartBodyBuilder.build();
    }

    private static MediaType createMediaType(String filename) {
        return MediaType.parse(URLConnection.getFileNameMap().getContentTypeFor(filename));
    }

    /**
     * build post params for type form
     *
     * @param paramsMap params map
     * @return request body
     */
    public static RequestBody buildPostForm(Map<String, Object> paramsMap) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                if (TextUtils.isEmpty(entry.getKey()))
                    continue;
                Object value = entry.getValue();
                formBodyBuilder.add(entry.getKey(), value == null ? "" : value.toString());
            }
        }
        return formBodyBuilder.build();
    }

    /**
     * build post params for type json
     *
     * @param paramsMap params map
     * @return request body
     */
    public static RequestBody buildPostJson(Map<String, Object> paramsMap) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonUtils.toJsonString(paramsMap));
    }

    /**
     * build get params
     *
     * @param paramsMap params map
     * @param url       url
     * @return new url
     */
    public static String buildGet(Map<String, Object> paramsMap, String url) {
        StringBuilder sb = null;
        if (paramsMap != null && !paramsMap.isEmpty()) {
            sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                if (TextUtils.isEmpty(entry.getKey()))
                    continue;
                sb.append(entry.getKey()).append('=');

                Object value = entry.getValue();
                try {
                    sb.append((value == null || TextUtils.isEmpty(value.toString())) ? "" :
                            URLEncoder.encode(value.toString(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append('&');
            }
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        if (sb != null && sb.length() > 0) {
            url += '?' + sb.toString();
        }
        return url;
    }

    /**
     * build request headers
     *
     * @param builder request builder
     * @param headers headers
     */
    public static void buildHeaders(Request.Builder builder, Map<String, Set<String>> headers) {
        if (builder == null)
            return;
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, Set<String>> entry : headers.entrySet()) {
                String name = entry.getKey();
                Set<String> values = entry.getValue();
                if (values != null) {
                    for (String value : values) {
                        builder.addHeader(name, value);
                    }
                }
            }
        }
    }
}
