package com.mybatisplus.controller;

import com.mybatisplus.entity.Admin;
import com.mybatisplus.service.IAdminService;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.ContinuousSessionContext;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
public class GruopSetAdmin {

    private volatile ArrayList<String> adminArray = new ArrayList<String>();

    @Autowired
    private IAdminService adminService;


    @Async
    @Listener
    @Filter(value = "nana权限设置", matchType = MatchType.TEXT_EQUALS)
    public void set_admin_before(GroupMessageEvent event, ContinuousSessionContext sessionContext) {
        final int time=30;
        var group = event.getGroup();
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
        String groupid = group.getId().toString();
        Admin admin = adminService.get_Admin_permission(accountCode);
        String account = admin.getAccount();

    if(Integer.parseInt(account)<1){
        adminArray.add(accountCode);
        event.replyAsync ( "请问您要将那个QQ号设置成管理员?");
        sessionContext.waitingForNextMessage(accountCode , GroupMessageEvent.Key, time, TimeUnit.SECONDS, (e, c) ->{
            if (!(c.getAuthor().getId().toString().equals(accountCode) && c.getGroup().getId().toString().equals(groupid ))) {
                return false;
            }
            if (e instanceof TimeoutException) {
                c.replyAsync("超时啦");
            }
            Admin admin1 = new Admin();
            admin1.setAccount(accountCode);
            admin1.setPermission("1");
            String s = adminService.set_Admin_permission(admin1);
            c.replyAsync( s);
            return true;
        });
    }else{
        event.replyAsync ( "您的那个权限不足 不能设置其他人的权限");
    }
        }



}
