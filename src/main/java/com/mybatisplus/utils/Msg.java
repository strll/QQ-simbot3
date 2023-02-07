package com.mybatisplus.utils;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.ID;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Message;
import love.forte.simbot.message.Text;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.utils
 * @date 2022/12/6 13:58
 */
@Slf4j
public class Msg {
//    public static String GroupImage(GroupMessageEvent event) {
//        String ImageUrl = "";
//        for (Message.Element<?> message : event.getMessageContent().getMessages()) {
//            if (message instanceof Image image) {
//                ImageUrl += image.getResource().getName();
//            }
//        }
//        return ImageUrl;
//    }

    public static void GroupMsg(GroupMessageEvent event) {
        for (Message.Element<?> message : event.getMessageContent().getMessages()) {
            if (message instanceof Text) {
                log.info("[文本消息: " + ((Text) message).getText() + " ]");
            }
        }
    }

    public static long Id(ID id) {
        return Long.parseLong(String.valueOf(id).trim());
    }

    public static ID Id(String id) {
        return ID.$(id.trim());
    }

    public static long longId(String time) {
        return Long.parseLong(time.trim());
    }
}
