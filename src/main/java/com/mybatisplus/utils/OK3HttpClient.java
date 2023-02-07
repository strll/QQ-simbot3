package com.mybatisplus.utils;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * @author mirai
 * &#064;className  okHttpClient
 * &#064;data  2023/01/15 17:19
 * &#064;description
 */


public class OK3HttpClient {
    private static final Logger log = LoggerFactory.getLogger(OK3HttpClient.class);

    public static String httpGet(String url, Map<String, Object> params, Map<String, String> headMap) {
        // 设置HTTP请求参数
        String result = null;
        url += getParams(params);
        var setHeaders = SetHeaders(headMap);
        var okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.headers(setHeaders);
        var request = builder.build();
        var call = okHttpClient.newCall(request);
        try {
            var response = call.execute();
            assert response.body() != null;
            //result = String.valueOf(new Gson().toJson(string));
            result= response.body().string();
        } catch (Exception e) {
            log.error("调用三方接口出错", e);
        }
        return result;
    }

    public static String getParams(Map<String, Object> params) {
        var sb = new StringBuilder("?");
        if (params != null) {
            params.forEach((key, value) -> {
                if (value != null) {
                    sb.append("&");
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                }
            });
            return sb.toString();
        } else {
            return "";
        }
    }

    public static Headers SetHeaders(Map<String, String> headersParams) {
        var headersbuilder = new Headers.Builder();
        if (headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next();
                headersbuilder.add(key, headersParams.get(key));
            }
        }
        return headersbuilder.build();
    }
}