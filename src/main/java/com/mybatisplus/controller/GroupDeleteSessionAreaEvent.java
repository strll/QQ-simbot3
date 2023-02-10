package com.mybatisplus.controller;


import com.mybatisplus.entity.Message;
import com.mybatisplus.service.IAdminService;
import com.mybatisplus.service.IMessageService;
import com.mybatisplus.utils.MyRedis;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.ID;
import love.forte.simbot.event.ContinuousSessionContext;
import love.forte.simbot.event.GroupMessageEvent;

import love.forte.simbot.message.Face;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component("GroupDeleteSessionAreaEvent")
public class GroupDeleteSessionAreaEvent implements ApplicationRunner {


    @Autowired
    private MyRedis redis;
    private HashSet<Integer> groupReply=new HashSet();

    @Autowired
    private IMessageService service;


    @Autowired
    private IAdminService adminService;


    @Async
    @Listener
    @Filter(value="nana关闭删除模块",matchType = MatchType.TEXT_EQUALS)
    public void stopStudy(GroupMessageEvent event) {
        ID id = event.getGroup().getId();
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                redis.setDelete("messagedelete",id.toString());
                boolean add = groupReply.remove(Integer.valueOf(id.toString()));
              //  Delete_flag = false;
                event.getSource().sendAsync("删除模块已经关闭");
            }
        }
    }

    @Async
    @Listener
    @Filter(value="nana启动删除模块",matchType = MatchType.TEXT_EQUALS)
    public void startStudy(GroupMessageEvent event) {

        ID id = event.getGroup().getId();
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号

        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                redis.setAdd("messagedelete",id.toString());
                boolean add = groupReply.add(Integer.valueOf(id.toString()));

                event.getSource().sendAsync("删除模块已经开启");

            }

        }
    }


    @Listener
    @Filter(value = "nana删除", matchType = MatchType.TEXT_EQUALS)
    public void testConversationDomain(GroupMessageEvent event, ContinuousSessionContext sessionContext) {

        if (groupReply.contains(Integer.valueOf( event.getGroup().getId().toString()))) {
            event.replyAsync("请问您要删除的关键词是什么?");
            ID groupId1 = event.getGroup().getId();
            ID id = event.getGroup().getId();
            String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
            final String qqId = String.valueOf(event.getAuthor().getId());
            final int time = 30;
            if (groupReply.contains(Integer.valueOf(id.toString()))) {

                sessionContext.waitingForNextMessage(qqId, GroupMessageEvent.Key, time, TimeUnit.SECONDS, (e, c) ->
                {
                    if ((c.getAuthor().getId().equals(id) && c.getGroup().getId().equals(groupId1))) {
                        return false;
                    }

                    if (e instanceof TimeoutException) {
                        c.replyAsync("超时啦");
                    }
                    Messages messages = c.getMessageContent().getMessages();
                    for (love.forte.simbot.message.Message.Element<?> element : messages) {
                        if (element instanceof Text) {
                            String text = ((Text) element).getText();
                            List<String> strings = service.Get_QQ_by_key(text); //根据要删除的内容返回这个key所有的qq号
                            int size = strings.size();
                            if (size != 0) {
                                for (String string : strings) {
                                    //如果这个qq号和触发这个函数的QQ号相同并且权限满足
                                    if (string.equals(c.getAuthor().getId().toString())) {
                                        int i = 0;
                                        int b = 0;
                                        i = service.DeleteMessage(text);
                                        if (b + i == 0) {
                                            c.replyAsync("删除失败");


                                        } else {
                                            c.replyAsync("删除成功");
                                        }
                                        break;
                                    } else {
                                        c.replyAsync("您的权限不足 只能管理员或者本人才能删除");
                                    }
                                }
                            }else {
                                c.replyAsync("未查询到您存储的关于\n"+text+"\n的信息");
                            }
                        }
                    }
                    return true;
                });
            }
        }else{
            event.replyAsync("该群管理员未开启删除模块");
        }
    }





    @Override
    public void run(ApplicationArguments args) throws Exception {
        Set<String> groupReply = redis.setfindAll("messagedelete");
        for (String s : groupReply) {
            this.groupReply.add(Integer.valueOf(s));
        }
    }

}
