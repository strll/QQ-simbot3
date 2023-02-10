package com.mybatisplus.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class GetNews {
            //因为信息过长所以需要以聊天列表的形式发送 经过测试图片和文字在一起容易被风控所以分开发送
    public  MiraiForwardMessageBuilder EveryDayNews(GroupMessageEvent event) throws IOException {
        MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readValue(new URL("https://www.zhihu.com/api/v4/columns/c_1261258401923026944/items"), JsonNode.class);
        String contentHtml = jsonNode.get("data").get(0).get("content").asText();

        Document parse = Jsoup.parse(contentHtml);
        Elements allElements = parse.getAllElements();
        for (Element element : allElements) {
            if("img".equals(element.tagName())){
                var result = new MessagesBuilder();
                String url = element.attr("src");
               result.image(Resource.of(new URL(url))).append("\n");
                miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(), result.build());
            }else if("p".equals(element.tagName())) {
                var result = new MessagesBuilder();
                String content = element.text().trim();
                result.append(content).append("\n");
                miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(), result.build());

            }
        }
        return  miraiForwardMessageBuilder;
    }
}
