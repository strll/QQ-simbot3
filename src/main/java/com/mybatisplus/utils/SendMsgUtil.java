package com.mybatisplus.utils;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.ID;
import love.forte.simbot.definition.Group;
import love.forte.simbot.definition.Member;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessageReceipt;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class SendMsgUtil {
    /**
     * 发送普通群消息
     *
     * @param event
     * @param msg
     * @return
     */
    public static MessageReceipt sendSimpleGroupMsg(GroupMessageEvent event, String msg) {
        return sendSimpleGroupMsg(event.getGroup(), msg);
    }

    /**
     * 发送普通群消息
     *
     * @param group
     * @param msg
     * @return
     */
    public static MessageReceipt sendSimpleGroupMsg(Group group, String msg) {
        msg = msg.trim();
        log.info("发送普通群消息[{}]:{}", group.getName(), msg);
        return group.sendBlocking(msg);
    }

    /**
     * 发送回复群消息
     *
     * @param event
     * @param msg
     * @return
     */
    public static MessageReceipt sendReplyGroupMsg(GroupMessageEvent event, String msg) {
        msg = msg.trim();
        Group group = event.getGroup();
        Member author = event.getAuthor();
        log.info("发送回复群消息[{}] ==> [{}]:{}", group.getName(), author.getNickOrUsername(), msg);
        return event.replyBlocking(msg);
    }

    /**
     * @param event
     * @param id
     * @param msg
     * @return
     * @throws MalformedURLException
     */

    public static MessageReceipt sendSimpleGroupImage(GroupMessageEvent event, ID id, String msg, String image) throws MalformedURLException {
        return sendSimpleGroupImage(event.getGroup(), id, msg, image);
    }


    /**
     * 发送图片消息
     */
    public static MessageReceipt sendSimpleGroupImage(Group group, ID id, String msg, String image) throws MalformedURLException {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.at(id);
        messagesBuilder.text("\n");
        messagesBuilder.text(msg);
        messagesBuilder.text("\n");
        messagesBuilder.image(Resource.of(new URL(image)));
        return group.sendBlocking(messagesBuilder.build());
    }
    public static MessageReceipt sendSimpleGroupImage(Group group,  String image) throws MalformedURLException {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.image(Resource.of(new URL(image)));
        return group.sendBlocking(messagesBuilder.build());
    }
    public static MessageReceipt sendSimpleGroupImage(Group group,  String msg, String image) throws MalformedURLException {
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        messagesBuilder.text(msg);
        messagesBuilder.text("\n");
        messagesBuilder.image(Resource.of(new URL(image)));
        return group.sendBlocking(messagesBuilder.build());
    }
}
