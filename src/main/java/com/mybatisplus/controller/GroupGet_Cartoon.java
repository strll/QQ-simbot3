package com.mybatisplus.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mybatisplus.utils.Get_Cartoon_By_Picture;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.ID;

import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.event.GroupMessageEvent;

import love.forte.simbot.message.Image;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.message.Text;
import love.forte.simbot.resources.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

@Controller
public class GroupGet_Cartoon {

    @Autowired
    private Get_Cartoon_By_Picture get_cartoon_by_picture;

    @Listener
    @Filter(value = "nana找番", matchType = MatchType.TEXT_STARTS_WITH)
    public void find(GroupMessageEvent event) throws Exception {
        ID id = event.getGroup().getId();
        event.replyAsync("正在查询 请稍后");

        MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();
        Messages messages = event.getMessageContent().getMessages();
        for (love.forte.simbot.message.Message.Element<?> element : messages) {
            if (element instanceof Image<?> image) {
                String name = image.getResource().getName();

                Document document = null;

                try {
                    document = Jsoup.connect("https://api.trace.moe/search?anilistInfo&url="+name)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                            .ignoreContentType(true)
                            .get();
                    Element body = document.body();
                    String body1 = document.select("body").toString().replace("</body>", "");
                    String s = body1.replaceAll("<body>", "");
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    JSONArray result = jsonObject.getJSONArray("result");
                    //      JSONObject o = result.getJSONObject(0);

                    for (int i = 0; i <result.size(); i++) {
                        JSONObject o1 = result.getJSONObject(i);
                        var  msg = new MessagesBuilder();
                        String native_name = o1.getJSONObject("anilist").getJSONObject("title").get("native").toString();
                        //synonyms
                        msg.append("native_name: "+native_name);
                        String synonyms = o1.getJSONObject("anilist").get("synonyms").toString();
                        msg.append("\nsynonyms: "+synonyms+"\n" );
                        String image1 = o1.getString("image").replaceAll("&amp;","&");
                        var  msgurl = new MessagesBuilder();
                        msgurl.image(Resource.of(new URL(image1)));
                        miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(), msg.build());
                        miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(), msgurl.build());
                    }

//                    for ( Object o : result) {
//                        var  msg = new MessagesBuilder();
//                        JSONObject o1 = (JSONObject) o;
//                        String native_name = o1.getJSONObject("anilist").getJSONObject("title").get("native").toString();
//                        //synonyms
//                        msg.append("\nnative_name: "+native_name);
//                        String synonyms = o1.getJSONObject("anilist").get("synonyms").toString();
//                        msg.append("\nsynonyms: "+synonyms+"\n" );
//                        String image1 = o1.getString("image").replaceAll("&amp;","&");
//                        msg.image(Resource.of(new URL(image1))).append("\n");
//                        miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(), msg.build());
//                    }
                event.getGroup().sendAsync(miraiForwardMessageBuilder.build());
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
            }




    }


    }

