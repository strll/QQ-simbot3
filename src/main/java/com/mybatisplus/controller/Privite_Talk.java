package com.mybatisplus.controller;

import com.mybatisplus.utils.GetChatGpt;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.FriendMessageEvent;
import love.forte.simbot.event.GroupMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
public class Privite_Talk {
    @Autowired
    private GetChatGpt getChatGpt;
    @Listener
    public void openAi(FriendMessageEvent event) throws Exception {
        String next = new Scanner(event.getMessageContent().getPlainText()).next();
        event.replyBlocking((getChatGpt.Get(next)));
    }
}
