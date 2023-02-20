package com.mybatisplus.controller;


import com.mybatisplus.service.IAdminService;
import com.mybatisplus.utils.GetMoYu;
import com.mybatisplus.utils.GetNews;
import com.mybatisplus.utils.Get_Cartoon_News;
import com.mybatisplus.utils.HistoryTody;

import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.component.mirai.message.MiraiMessageContent;

import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashSet;

@Controller
public class GroupTimeSend {
    @Autowired
    private Get_Cartoon_News cartoon_news;
    private HashSet<GroupMessageEvent> hashset=new HashSet();
    @Autowired
    private IAdminService adminService;



    private volatile boolean ds_flag=true; //定时模块启动标志
    @Autowired
    private GetMoYu moyu;




    @org.springframework.scheduling.annotation.Async
    @Listener
    @Filter(value="nana添加群定时",matchType = MatchType.TEXT_EQUALS)
    public void addtime(GroupMessageEvent event) throws IOException {
        var group = event.getGroup();
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
        String groupid = group.getId().toString();
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {
             boolean add = hashset.add(event);
            if (add){
                event.replyAsync ("添加成功");
            }else{
                event.replyAsync ("添加失败");
            }
        }
    }
    @org.springframework.scheduling.annotation.Async
    @Listener
    @Filter(value="nana取消群定时",matchType = MatchType.TEXT_EQUALS)
    public void removetime(GroupMessageEvent event) throws IOException {
        var group = event.getGroup();
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
        String groupid = group.getId().toString();
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {
            boolean add = hashset.remove(event);
            if (add){
                event.replyAsync ("取消成功");
            }else{
                event.replyAsync ("取消失败");
            }
        }

    }

    @Async
    @Listener
    @Filter(value="nana关闭定时模块",matchType = MatchType.TEXT_EQUALS)
    public void ds(GroupMessageEvent event) {
        var group = event.getGroup();
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
        String groupid = group.getId().toString();
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {
            synchronized (this) {
                ds_flag = false;
                event.replyAsync ("定时模块已经关闭");
            }

        }
    }
    @Async
    @Listener
    @Filter(value="nana启动定时模块",matchType = MatchType.TEXT_EQUALS)
    public void dso(GroupMessageEvent event) {
        var group = event.getGroup();
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
        String groupid = group.getId().toString();
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {
            synchronized (this) {
                ds_flag = true;
                event.replyAsync ("定时模块已经开启");
            }

        }
    }



    @Autowired
    private HistoryTody historyTody;
    @Scheduled(cron="0 0 7 * * * ")
    public void historyTody(){
        if(ds_flag) {
            for (GroupMessageEvent event :  hashset) {
                 String historytody = historyTody.historytody();
                String[] split = historytody.split("end");
                MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();

                for (String s : split) {
                    var messagesBuilder = new MessagesBuilder();
                    messagesBuilder.append(s);
                    miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(),messagesBuilder.build());
                }
                event.getSource().sendBlocking("早上好,这是历史上的今天");
                // 发送消息
                event.getSource().sendAsync( miraiForwardMessageBuilder.build());
            }
        }

    }

    @Scheduled(cron="0 30 7 * * *")
    public void moyu(){
        if(ds_flag) {
            for (GroupMessageEvent event :  hashset) {
                MessagesBuilder moyu = this.moyu.getMoyu();
                event.getSource().sendAsync(moyu.build());
            }


        }
    }


    @Autowired
    private GetNews getNews;
    @Scheduled(cron="0 31 7 * * * ")
    public void sendNews() throws IOException {
        if(ds_flag) {
            for (GroupMessageEvent event :  hashset) {
                MiraiForwardMessageBuilder miraiForwardMessageBuilder = getNews.EveryDayNews(event);
                event.getSource().sendAsync(miraiForwardMessageBuilder.build());
            }

        }
    }


    @Scheduled(cron="0 32 7 * * 7 ")
    public void sendNews1() throws IOException {

        if(ds_flag) {
            String news = cartoon_news.getNews();
            String[] ends = news.split("end");


            for (GroupMessageEvent event :  hashset) {
                event.getSource().sendAsync("早上好 这是本周的动漫资讯");

                MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();
                var messagesBuilder = new MessagesBuilder();
                for (String s : ends) {
                    messagesBuilder.append(s);
                    miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(),messagesBuilder.build());
                }
            }
            }
        }
    }

