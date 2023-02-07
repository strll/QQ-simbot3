package com.mybatisplus.utils;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.utils
 * @date 2022/12/22 11:46
 */

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class HttpUtil {

    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static String sendRequest(String url, Map<String, String> params, String method, String data, Map<String, String> headers) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(url);
        if (params != null) {
            for (Map.Entry<String, String> strEntry : params.entrySet()) {
                stringBuilder.append(strEntry.getKey()).append("=").append(strEntry.getValue()).append("&");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        Request.Builder requestBuilder = new Request.Builder().url(stringBuilder.toString());
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if ("POST".equals(method)) {
            requestBuilder.post(RequestBody.create(data, JSON));
        }

        Request request = requestBuilder.build();
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            return response.body().string();
        }
    }

    public static String sendGet(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        return client.newCall(request).execute().request().toString();
    }
}