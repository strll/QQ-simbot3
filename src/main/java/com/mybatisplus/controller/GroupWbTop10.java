package com.mybatisplus.controller;


import com.mybatisplus.utils.GetWbTop10;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContent;

import love.forte.simbot.event.ContinuousSessionContext;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.message.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
public class GroupWbTop10 {
    @Autowired
    private GetWbTop10 getWbTop10;

    @Listener
    @Filter(value="nana微博热搜",matchType = MatchType.TEXT_EQUALS)
    @Async
    public void sendNews(GroupMessageEvent event, ContinuousSessionContext sessionContext) throws IOException {
        var group = event.getGroup();
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
        String groupid = group.getId().toString();
        final int time = 30;
        String wbTop10 = getWbTop10.getWbTop10();
        MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();

        String[] ends = wbTop10.split("end");

        for (String end : ends) {
            var messagesBuilder = new MessagesBuilder();
            messagesBuilder.append(end);
            miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(),messagesBuilder.build());
        }

    event.getSource().sendAsync(miraiForwardMessageBuilder.build());
    }

}
