package com.mybatisplus.utils.app;

import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Get_Picture_in_Text_app {
    public StringBuilder  get (String url){
        String re ="";
        String regex = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\&%\\+\\$#_=]*)?";
        // 创建正则表达式对象
        Pattern r = Pattern.compile(regex);
        // 创建匹配器
        Matcher m = r.matcher(url);
        // 查找匹配的字符串
        int i=0;
        String s;
        StringBuilder  messagesBuilder = new  StringBuilder();

        while (m.find()) {
                messagesBuilder.append("https://mini.s-shot.ru/1024x768/PNG/800/?"+m.group());

        }

        return messagesBuilder;
    }
}
