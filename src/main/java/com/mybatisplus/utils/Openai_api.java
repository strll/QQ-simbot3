package com.mybatisplus.utils;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
@Log4j2
@Component
@NoArgsConstructor
public class Openai_api {
    @Value("${openai_api}")
    private String key;
    private  String doPost(String url, String body) {
        // post请求
        CloseableHttpClient httpClient;
        HttpPost httpPost;
        HttpResponse response;
        String responseContent;
        try {
            // 创建 httpClient
            httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Bearer " + key);
            httpPost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
            response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            responseContent = EntityUtils.toString(httpEntity, "UTF-8");
            EntityUtils.consume(httpEntity);
            return responseContent;
        } catch (IOException e) {
            log.info("异常消息:" + e.getMessage());
            return "";
        }

    }
    public  String getPost(String url, String params) {
        return doPost(url, params);
    }
}
