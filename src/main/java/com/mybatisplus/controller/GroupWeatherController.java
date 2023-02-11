package com.mybatisplus.controller;

import com.mybatisplus.entity.Message;
import com.mybatisplus.service.GetWeatherService;
import com.mybatisplus.service.IAdminService;

import com.mybatisplus.utils.GetNowWeather;
import com.mybatisplus.utils.GetWeather;
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
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
public class GroupWeatherController {





    @Autowired
    private GetWeather getWeather;

    @Listener
    @Filter(value = "nana天气", matchType = MatchType.TEXT_EQUALS)

    public void testConversationDomain(GroupMessageEvent event, ContinuousSessionContext sessionContext) throws Exception{

        var group = event.getGroup();
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
        String groupid = group.getId().toString();


        event.getSource().sendAsync("请输入您要查询的城市:");

            // 发送提示信息
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
                    c.replyAsync(text+"近日天气如下");
                    HashMap<Integer, HashMap<String, String>> betterWeather=null;
                    try {
                        betterWeather = getWeather.getBetterWeather(text.getText());
                        HashMap<Integer, HashMap<String, String>> finalBetterWeather = betterWeather;
                        MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();
                        for (int i = 0; i < 3; i++) {
                            StringBuffer re=new StringBuffer();
                            HashMap<String, String> hashMap = finalBetterWeather.get(i);
                            var messagesBuilder = new MessagesBuilder();
                            messagesBuilder.append("")
                                    .append("预报日期: ").append(hashMap.get("预报日期")).append("\n")
                                    .append("最高温度: ").append(hashMap.get("最高温度")).append("\n")
                                    .append("最低温度: ").append(hashMap.get("最低温度")).append("\n")
                                    .append("白天天气状况: ").append(hashMap.get("白天天气状况")).append("\n")
                                    .append("夜晚天气状况: ").append(hashMap.get("夜晚天气状况")).append("\n")
                                    .append("白天风向: ").append(hashMap.get("白天风向")).append("\n")
                                    .append("白天风力等级: ").append(hashMap.get("白天风力等级")).append("\n")
                                    .append("夜间风向: ").append(hashMap.get("夜间当天风向")).append("\n")
                                    .append("夜间风力等级: ").append(hashMap.get("夜间风力等级")).append("\n")
                                    .append("能见度: ").append(hashMap.get("能见度")).append("公里\n")
                                    .append("相对湿度: ").append(hashMap.get("相对湿度")).append("\n")
                                    .append("当天总降水量: ").append(hashMap.get("当天总降水量")).append("\n");
                            miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(),messagesBuilder.build());

                        }
                        event.getSource().sendAsync(miraiForwardMessageBuilder.build());

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }
            return true;
        });
    }




}
