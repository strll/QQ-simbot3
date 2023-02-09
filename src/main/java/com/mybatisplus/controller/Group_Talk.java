package com.mybatisplus.controller;


import com.mybatisplus.entity.Groupid_and_Authorid;
import com.mybatisplus.service.IAdminService;
import com.mybatisplus.utils.Get_Talk;
import com.mybatisplus.utils.MyRedis;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.ID;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Message;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Controller
public class Group_Talk  implements ApplicationRunner {

    @Autowired
    private Get_Talk getTalk;
    @Autowired
    private IAdminService adminService;

    @Autowired
    private MyRedis myRedis;

    private HashSet<Groupid_and_Authorid> hashset=new HashSet<Groupid_and_Authorid>();
    private HashSet<Long> group=new HashSet<>();

    @Listener
    @Filter(value="nana开启聊天模块",matchType = MatchType.TEXT_EQUALS)
    public void open(GroupMessageEvent event) throws IOException {
        ID id = event.getAuthor().getId();
        ID groupid = event.getGroup().getId();

        if (Integer.parseInt(adminService.get_Admin_permission(id.toString()).getPermission()) == 0) {
            String group_talk = myRedis.setAdd("group_talk", groupid + "");
if (group_talk.equals("1")){
    event.replyAsync("开启成功");
}
            group.clear();
            Set<String> groupReply = myRedis.setfindAll("group_talk");
            for (String s : groupReply) {
                this.group.add(Long.valueOf(s));
            }

        }
    }

    @Listener
    @Filter(value="nana关闭聊天模块",matchType = MatchType.TEXT_EQUALS)
    public void cl(GroupMessageEvent event) throws IOException {
        String id = event.getAuthor().getId().toString(); //获取发送人的QQ号
        ID groupid = event.getGroup().getId();
        if (Integer.parseInt(adminService.get_Admin_permission(id).getPermission()) == 0) {
            myRedis.setDelete("group_talk",groupid+"");
            group.clear();

            Set<String> groupReply = myRedis.setfindAll("group_talk");
            for (String s : groupReply) {
                this.group.add(Long.valueOf(s));
            }

        }
    }

    @org.springframework.scheduling.annotation.Async
    @Listener
    @Filter(value="nana聊天",matchType = MatchType.TEXT_EQUALS)
    public void sendNews(GroupMessageEvent event) throws IOException {
            String id = event.getAuthor().getId().toString(); //获取发送人的QQ号
            String groupid = event.getGroup().getId().toString();
        Groupid_and_Authorid groupid_and_authorid = new Groupid_and_Authorid(groupid, id);
        if (group.contains(Long.valueOf(groupid))) {

            boolean add = hashset.add(groupid_and_authorid);
            if (add) {
                event.replyAsync("您已进入nana的聊天模式,如果想要退出请输入 nana退出聊天");
            } else {
                event.replyAsync("进入聊天模式失败");
            }
        }else{
            event.replyAsync("未开启该模块");
        }


    }
    //==================================
    @org.springframework.scheduling.annotation.Async
    @Listener
    @Filter(value="nana退出聊天",matchType = MatchType.TEXT_EQUALS)
    public void removesend(GroupMessageEvent event) throws IOException {

        String id = event.getAuthor().getId().toString(); //获取发送人的QQ号
        String groupid = event.getGroup().getId().toString();
        if (group.contains( Long.valueOf(groupid))) {

            boolean add = hashset.remove(new Groupid_and_Authorid(groupid, id));
            if (add) {
                event.replyAsync("您已退出nana的聊天模式");
            } else {
                event.replyAsync("退出聊天模式失败");
            }
        }else{
            event.replyAsync("未开启该模块");
        }

    }

    @Async
    @Listener
    public void onGroupMsg(GroupMessageEvent event) throws IOException {
        String id = event.getAuthor().getId().toString(); //获取发送人的QQ号
        String groupid = event.getGroup().getId().toString();
        if (group.contains( Long.valueOf(groupid))) {

            Groupid_and_Authorid groupid_and_authorid = new Groupid_and_Authorid(groupid, id);
            String nickOrUsername = event.getAuthor().getNickOrUsername();
            String s = "";
            if (hashset.contains(groupid_and_authorid)) {
                Messages messages = event.getMessageContent().getMessages();

                for (Message.Element<?> message : messages) {
                    if (message instanceof Text) {
                        //     log.info(MessageFormat.format("[文字消息: {0} ]", ((Text) message).getText().substring(7)));
                        s = s + ((Text) message).getText();

                    }
                }
                String talk = getTalk.get_talk(s);
                event.replyAsync(talk);

            }
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Set<String> groupReply = myRedis.setfindAll("group_talk");
        for (String s : groupReply) {
            this.group.add(Long.valueOf(s));
        }
    }
}
