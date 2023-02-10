package com.mybatisplus.controller;


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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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

        // 通过账号拼接一个此人在此群中的唯一key

        Messages messages = event.getMessageContent().getMessages();
        for (love.forte.simbot.message.Message.Element<?> element : messages) {
            if (element instanceof Image<?> image) {
                String name = image.getResource().getName();
                MiraiForwardMessageBuilder cartoon = get_cartoon_by_picture.getCartoon(event,name);
                event.getSource().sendAsync(cartoon.build());
            }
            }




    }


    }

