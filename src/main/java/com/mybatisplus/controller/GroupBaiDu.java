package com.mybatisplus.controller;

import com.mybatisplus.utils.GetBaiDu;
import com.mybatisplus.utils.MakeNeko;

import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Message;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.message.Text;
import love.forte.simbot.resources.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

@Controller
public class GroupBaiDu {
    @Autowired
    private GetBaiDu getBaiDu;


    @Listener
    @Filter(value = "nana查询",matchType = MatchType.TEXT_STARTS_WITH)
    public void listenSong1(GroupMessageEvent event) throws IOException {
        for (Message.Element<?> message : event.getMessageContent().getMessages()) {
            if(message instanceof Text){
                MessagesBuilder messagesBuilder = new MessagesBuilder();
                String text = ((Text) message).getText().substring(6);
                String yiqing="";
                HashMap baiDu=null;
                try {
                    baiDu = getBaiDu.getBaiDu(text);
                }catch (Exception e){
                    event.replyAsync("查询失败");
                }
                StringBuffer stringBuffer = new StringBuffer();
                messagesBuilder.append((String)baiDu.get("title")).append("\n").append((String) baiDu.get("content")).append("\n"
                );
                String img = (String) baiDu.get("img");
                if(!img.equals("无"))
                {

                    messagesBuilder.image(Resource.of(new URL((String) baiDu.get("img"))));
                }
                MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();
                miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(),messagesBuilder.build());
                event.getSource().sendAsync(miraiForwardMessageBuilder.build());
            }
        }


    }

    @Listener
    @Filter(value = "nana百度",matchType = MatchType.TEXT_STARTS_WITH)
    public void listenSong12(GroupMessageEvent event) throws IOException {
        for (Message.Element<?> message : event.getMessageContent().getMessages()) {

            if (message instanceof Text) {
                String yiqing="";
                MessagesBuilder baiDu=null;
                String text = ((Text) message).getText().substring(6);
                try {
                    MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();
                    baiDu = getBaiDu.getChaXun(text);

                    miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(),baiDu.build() );
                    event.getSource().sendAsync( miraiForwardMessageBuilder.build());
                }catch (Exception e){
                    event.getSource().sendAsync("查询百度失败");
                }
            }

        }



    }


}
