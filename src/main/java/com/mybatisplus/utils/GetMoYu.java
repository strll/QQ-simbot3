package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONObject;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class GetMoYu {

    public MessagesBuilder getMoyu() {
        var messagesBuilder = new MessagesBuilder();
        try {


            Connection.Response host = Jsoup.connect("https://api.emoao.com/api/moyu?type=json")
                    .header("Content-Type","application/json")
                    .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                    .header("Accept", "text/html, application/xhtml+xml, */*")
                    .ignoreContentType(true)
                    .execute();


            String body = host.body();
            JSONObject jsonObject = JSONObject.parseObject(body);
            String string = jsonObject.getString("title");
            String string1 = jsonObject.getString("imgurl");
            messagesBuilder.append(string).image(Resource.of(new URL(string1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    return messagesBuilder;

    }
}

