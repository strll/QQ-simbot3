package com.mybatisplus.controller;


import com.mybatisplus.entity.MyGroupMessageEvent_TimeSend;
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
    private HashSet<MyGroupMessageEvent_TimeSend> hashset=new HashSet();
    @Autowired
    private IAdminService adminService;



    private static volatile boolean ds_flag=true; //定时模块启动标志
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
            MyGroupMessageEvent_TimeSend myGroupMessageEvent_timeSend = new MyGroupMessageEvent_TimeSend(event);
            boolean add = hashset.add(myGroupMessageEvent_timeSend);
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
            MyGroupMessageEvent_TimeSend myGroupMessageEvent_timeSend = new MyGroupMessageEvent_TimeSend(event);
            boolean add = hashset.remove(myGroupMessageEvent_timeSend);
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

            for (MyGroupMessageEvent_TimeSend event :  hashset) {
                GroupMessageEvent groupMessageEvent = event.getGroupMessageEvent();
                String historytody = historyTody.historytody();
                String[] split = historytody.split("end");
                MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();

                for (String s : split) {
                    var messagesBuilder = new MessagesBuilder();
                    messagesBuilder.append(s);
                    miraiForwardMessageBuilder.add( groupMessageEvent.getBot().getId(), groupMessageEvent.getBot().getUsername(),messagesBuilder.build());
                }
                groupMessageEvent.getSource().sendBlocking("早上好,这是历史上的今天");
                // 发送消息
                int x=0;
                try {
                    x=(int)(Math.random()*20000+1);
                    wait(x);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                groupMessageEvent.getSource().sendAsync( miraiForwardMessageBuilder.build());
            }
        }

    }

    @Scheduled(cron="0 30 7 * * *")
    public void moyu(){
        if(ds_flag) {

            for (MyGroupMessageEvent_TimeSend event :  hashset) {
                GroupMessageEvent groupMessageEvent = event.getGroupMessageEvent();
                MessagesBuilder moyu = this.moyu.getMoyu();
                int x=0;
                try {
                    x=(int)(Math.random()*20000+1);
                    wait(x);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                groupMessageEvent.getSource().sendAsync(moyu.build());
            }


        }
    }


    @Autowired
    private GetNews getNews;
    @Scheduled(cron="0 31 7 * * * ")
    public void sendNews() throws IOException {
        if(ds_flag) {
            for (MyGroupMessageEvent_TimeSend event :  hashset) {
                GroupMessageEvent groupMessageEvent = event.getGroupMessageEvent();
                MiraiForwardMessageBuilder miraiForwardMessageBuilder = getNews.EveryDayNews(groupMessageEvent);
                int x=0;
                try {
                    x=(int)(Math.random()*20000+1);
                    wait(x);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                groupMessageEvent.getSource().sendAsync(miraiForwardMessageBuilder.build());
            }

        }
    }


    @Scheduled(cron="0 32 7 * * 7 ")
    public void sendNews1() throws IOException {

        if(ds_flag) {
            String news = cartoon_news.getNews();
            String[] ends = news.split("end");


            for (MyGroupMessageEvent_TimeSend event :  hashset) {
                GroupMessageEvent groupMessageEvent = event.getGroupMessageEvent();
                int x=0;
                try {
                    x=(int)(Math.random()*20000+1);
                    wait(x);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                groupMessageEvent.getSource().sendAsync("早上好 这是本周的动漫资讯");

                MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();
                var messagesBuilder = new MessagesBuilder();
                for (String s : ends) {
                    messagesBuilder.append(s);
                    miraiForwardMessageBuilder.add( groupMessageEvent.getBot().getId(), groupMessageEvent.getBot().getUsername(),messagesBuilder.build());
                }
            }
            }
        }
    }

