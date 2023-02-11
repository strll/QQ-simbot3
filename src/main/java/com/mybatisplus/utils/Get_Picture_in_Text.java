package com.mybatisplus.utils;

import com.mybatisplus.utils.Qiniuyun.ResponsePojo.Resopnse_Qiniuyun_JsonRootBean;
import com.qiniu.common.QiniuException;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Get_Picture_in_Text {
    public MessagesBuilder get (String url){
        String re ="";
        String regex = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\&%\\+\\$#_=]*)?";
        // 创建正则表达式对象
        Pattern r = Pattern.compile(regex);
        // 创建匹配器
        Matcher m = r.matcher(url);
        // 查找匹配的字符串
        int i=0;
        String s;
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        while (m.find()) {
            try {
                messagesBuilder.image(Resource.of(new URL("https://mini.s-shot.ru/1024x768/PNG/800/?"+m.group())));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }

        return messagesBuilder;
    }
}
