package com.mybatisplus.controller;


import com.mybatisplus.utils.Get_Cartoon_News;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import love.forte.simboot.annotation.Listener;
import java.io.IOException;

@Controller
public class Group_Cartoon_News {
    @Autowired
    private Get_Cartoon_News cartoon_news;

    @Listener
    @Filter(value = "nana动漫资讯", matchType = MatchType.TEXT_EQUALS)
    public void sendnews(GroupMessageEvent event) throws IOException {
        String news = cartoon_news.getNews();
        String[] ends = news.split("end");
        MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();
        for (String end : ends) {
            var messagesBuilder = new MessagesBuilder();
            messagesBuilder.append(end);
            miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(),messagesBuilder.build());
        }
        event.getSource().sendBlocking(miraiForwardMessageBuilder.build());
    }
}
