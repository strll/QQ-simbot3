package com.mybatisplus.utils;

import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cat_to_message {
    public static MessagesBuilder getMessage(String message) throws MalformedURLException {
        var messagesBuilder = new MessagesBuilder();
        String pattern = "https?://[^\\s]+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(message);
        String[] split =message.split("\\[");
        String text1=  split[0];
        String group="";
        if(m.find()){
            group = m.group();
        }
        String s = group.replaceAll("]", "");
        messagesBuilder.append(text1);
        try {
            messagesBuilder.image(Resource.of(new URL(s)));
        }catch (Exception e){
        }

       return messagesBuilder;
    }
}
