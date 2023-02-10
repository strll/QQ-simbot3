package com.mybatisplus.controller;

import com.mybatisplus.utils.GetMoYu;

import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;

@Controller
public class GroupMoYu {
@Autowired
private GetMoYu moyu;
    @Listener
    @Filter(value="nana摸鱼日历",matchType = MatchType.TEXT_EQUALS)
    @Async
    public void sendmoyu(GroupMessageEvent event) throws IOException {
        var messagesBuilder = new MessagesBuilder();
        Messages build = moyu.getMoyu().build();
        event.getSource().sendAsync(build);

    }
}
