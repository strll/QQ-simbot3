package com.mybatisplus.controller;

import com.mybatisplus.utils.HistoryTody;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;

import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;

import love.forte.simbot.event.GroupMessageEvent;

import love.forte.simbot.message.MessagesBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class GroupHistoryTody {


    @Autowired
    private HistoryTody historyTody;

    @Listener
    @Filter(value="nana历史上的今天",matchType = MatchType.TEXT_EQUALS)
    @Async
    public void sendNews(GroupMessageEvent event) throws IOException {
         String historytody = historyTody.historytody();
        MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();



            String[] split = historytody.split("end");
            for (String s : split) {
                var messagesBuilder = new MessagesBuilder();
                messagesBuilder.append(s);
                miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(),messagesBuilder.build());
            }



        event.getSource().sendAsync("您好 这是历史上的今天");
        // 发送消息
        event.getSource().sendAsync( miraiForwardMessageBuilder.build());
    }
}
