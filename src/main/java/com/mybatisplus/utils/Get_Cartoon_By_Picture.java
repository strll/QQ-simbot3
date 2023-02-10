package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class Get_Cartoon_By_Picture {
    public  MiraiForwardMessageBuilder getCartoon(GroupMessageEvent event,String url) throws Exception{
        /**
         * https://media.trace.moe/image/20519/%5BDHR%26Hakugetsu%5D%5BTamako%20Love%20Story%5D%5BMovie%5D%5BBIG5%5D%5B720P%5D%5BAVC_AAC%5D.mp4.jpg?t=4786.665&now=1663070400&token=QYDnO7TBVwHkzLLn2vEjWPv2Jvg±〖番剧编号〗
        **/


        MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();
        Document document = null;

        try {
            document = Jsoup.connect("https://api.trace.moe/search?anilistInfo&url="+url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                    .ignoreContentType(true)
                    .get();
            Element body = document.body();
            String body1 = document.select("body").toString().replace("</body>", "");
            String s = body1.replaceAll("<body>", "");
            JSONObject jsonObject = JSONObject.parseObject(s);
            JSONArray result = jsonObject.getJSONArray("result");
      //      JSONObject o = result.getJSONObject(0);
             for ( Object o : result) {
                var  msg = new MessagesBuilder();
                JSONObject o1 = (JSONObject) o;
                String native_name = o1.getJSONObject("anilist").getJSONObject("title").get("native").toString();
                //synonyms
                msg.append("native_name:"+native_name);
                String synonyms = o1.getJSONObject("anilist").get("synonyms").toString();
                msg.append("synonyms:"+synonyms );
                String image = o1.getString("image").replaceAll("&amp;","&");
                msg.image(Resource.of(new URL(image))).append("\n");
                miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(), msg.build());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return   miraiForwardMessageBuilder;


//        MessagesBuilder messagesBuilder = new MessagesBuilder();
//
//        Document document = Jsoup.connect("http://apii.gq/api/trace.php?type=text&url="+url+"&count=1").get();
//        Elements body = document.select("body");
//        String text = body.text();
//        String replace = text.replace("〖", "\n〖");
//        String[] split =replace.split("±img=");
//        String[] split1 = split[1].split("±");
//        String[] split2 = split1[1].split("±");
//
//        messagesBuilder .image(Resource.of(new URL(split1[0]))).append("\n").append(split2[0]);
//        return  messagesBuilder;
    }
}
