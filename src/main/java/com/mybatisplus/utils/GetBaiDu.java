package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONObject;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.HashMap;

import static java.awt.SystemColor.info;

@Component
public class GetBaiDu {

    public MessagesBuilder getChaXun(String text)throws Exception{
        //https://api.wer.plus/api/dub?t=

        MessagesBuilder messagesBuilder = new MessagesBuilder();
        Connection.Response execute = Jsoup.connect("https://api.wer.plus/api/dub?t="+text)
                .postDataCharset("UTF-8")
                .ignoreContentType(true)
                .execute();
        String body = execute.body();
        JSONObject jsonObject = JSONObject.parseObject(body);

        JSONObject data = jsonObject.getJSONObject("data");

        String text1 = data.getString("text");
        String  img_url = data.getString("img_url");

        messagesBuilder.append("您搜索的是:").append(text).append("\n")
                .append("查询内容是: ").append(text1).append("\n")
                .image(Resource.of(new URL(img_url)));


        return messagesBuilder;
    }

    public HashMap getBaiDu(String text)throws Exception{
        //https://wenxin110.top/api/sg_encyclopedia?text=python&type=json
        HashMap<String, String> map = new HashMap<>();
        Connection.Response execute = Jsoup.connect("https://wenxin110.top/api/sg_encyclopedia?text="+text+"&type=json")
                .postDataCharset("UTF-8")
                .ignoreContentType(true)
                .execute();
        String body = execute.body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        String content1 = jsonObject.getString("content");
        String img = jsonObject.getString("img");
        String title = jsonObject.getString("title");
        map.put("title", title);
        map.put("img",img);
        map.put("content",content1);
        return map;
    }
}
