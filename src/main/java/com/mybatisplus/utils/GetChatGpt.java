package com.mybatisplus.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mybatisplus.entity.ChatGptBean.ChatGptBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GetChatGpt {
    @Value("${ChatGpt.url}")
    private String url;
    @Value("${ChatGpt.key}")
    private String key;

    public String Get(String messageContent) {
        String model = "gpt-3.5-turbo";

        JSONObject requestBody = new JSONObject();
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", messageContent);
        messages.add(message);
        requestBody.put("model", model);
        requestBody.put("messages", messages);
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .header("Content-Type", "application/json")
                    .header("Connection","keep-alive")
                    .header("Authorization", "Bearer " + key)
                    .requestBody(requestBody.toString())
                    .timeout(40*1000*200)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .post();
        } catch (IOException e) {

            return "ChatGpt出现了一些小问题";
        }
        Element body = document.body();
        String s = body.toString();
        String s1 = s.replaceAll("<body>", "").replaceAll("</body>", "");
        ChatGptBean chatGptBean = JSON.parseObject(s1, ChatGptBean.class);
        String content = chatGptBean.getChoices().get(0).getMessage().getContent();
        String replace = content.replace("\n\n", "");
        return replace;
    }
}
