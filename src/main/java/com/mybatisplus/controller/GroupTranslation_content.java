package com.mybatisplus.controller;



import com.mybatisplus.utils.GetTranslation_Content;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.ContinuousSessionContext;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
public class GroupTranslation_content {
@Autowired
private GetTranslation_Content getTranslation_Content;



    @Listener
    @Filter(value="nana翻译",matchType = MatchType.TEXT_EQUALS)
    public void sendNews1(GroupMessageEvent event, ContinuousSessionContext sessionContext) throws Exception {
        event.replyAsync ("请问您要翻译的是什么?");

        var group = event.getGroup();
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
        String groupid = group.getId().toString();
        final int time = 30;
        sessionContext.waitingForNextMessage(accountCode , GroupMessageEvent.Key, time, TimeUnit.SECONDS, (e, c) ->{
            if (!(c.getAuthor().getId().toString().equals(accountCode) && c.getGroup().getId().toString().equals(groupid ))) {
                return false;
            }
            if (e instanceof TimeoutException) {
                c.replyAsync("超时啦");
            }
            Messages messages1 = c.getMessageContent().getMessages();
            for (love.forte.simbot.message.Message.Element<?> element :messages1) {
                if(element instanceof Text text){
                    try {
                        String translation_content = getTranslation_Content.getTranslation_content(text.getText());
                        c.replyAsync (translation_content);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return true;
        });

    }



}
