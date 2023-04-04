package com.mybatisplus.controller;

import com.mybatisplus.utils.GetNews;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;

import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;

import love.forte.simbot.component.mirai.message.MiraiSendOnlyForwardMessage;
import love.forte.simbot.event.GroupMessageEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class GroupNews {

    @Autowired
    private GetNews getNews;
    @Async
    @Listener
    @Filter(value="nana每日新闻",matchType = MatchType.TEXT_EQUALS)
    public void sendNews(GroupMessageEvent event) throws IOException {
        MiraiForwardMessageBuilder miraiForwardMessageBuilder = getNews.EveryDayNews(event);
        MiraiSendOnlyForwardMessage build = miraiForwardMessageBuilder.build();
        event.getGroup().sendBlocking(build);

//        event.getSource().sendAsyn(build);
    }
    @Async
    @Listener
    @Filter(value="nana每日新闻无图",matchType = MatchType.TEXT_EQUALS)
    public void sendNews_safe(GroupMessageEvent event) throws IOException {
        MiraiForwardMessageBuilder miraiForwardMessageBuilder = getNews.EveryDayNews_safe(event);
        MiraiSendOnlyForwardMessage build = miraiForwardMessageBuilder.build();
        event.getGroup().sendBlocking(build);
    }

}
