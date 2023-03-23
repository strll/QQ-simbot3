package com.mybatisplus.controller;


import com.mybatisplus.entity.Message;
import com.mybatisplus.service.IAdminService;
import com.mybatisplus.service.IMessageService;
import com.mybatisplus.utils.MakeNeko;
import com.mybatisplus.utils.MyRedis;
import com.mybatisplus.utils.Send_To_minio;

import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.ContinuousSessionContext;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Image;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Transactional
@Component()
public class GroupStudySessionAreaEvent implements ApplicationRunner {

    @Autowired
    private Send_To_minio send_to_minio;

    @Autowired
    private MyRedis redis;
    
    @Autowired
    private IMessageService service;
    Message message = null;
    HashMap<String,Message> hashMap=new HashMap<String,Message>();
    
  //  private volatile boolean Study_flag=false; //学习模块启动标志
    @Autowired
    private IAdminService adminService;
    private HashSet<Integer> groupReply=new HashSet();

    @Async
    @Listener
    @Filter(value="nana关闭学习模块",matchType = MatchType.TEXT_EQUALS)
    public void stopStudy(GroupMessageEvent event) {
        var group = event.getGroup();
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
        String groupid = group.getId().toString();
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                String groupStudy = redis.setDelete("groupStudy", groupid);

                this.groupReply.remove(Integer.valueOf(groupid) );

                if (Integer.parseInt(groupStudy)==0) {
                    event.replyAsync("学习模块关闭失败");
                }else{
                    event.replyAsync("学习模块已经关闭");
                }
            }

        }
    }
    @Async
    @Listener
    @Filter(value="nana启动学习模块",matchType = MatchType.TEXT_EQUALS)
    public void startStudy(GroupMessageEvent event) {
        var group = event.getGroup();
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
        String groupid = group.getId().toString();
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) == 0) {
            synchronized (this) {
                redis.setAdd("groupStudy",groupid);
                //Study_flag = true;
                groupReply.add(Integer.valueOf(groupid ));
                event.replyAsync("学习模块已经开启");
            }

        }
    }


  @Listener
    @Filter(value = "nana学习", matchType = MatchType.TEXT_EQUALS)

    @Async
    public void testConversationDomain(GroupMessageEvent event, ContinuousSessionContext sessionContext) {
      var group = event.getGroup();
      String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
      String groupid = group.getId().toString();
      Messages messages = event.getMessageContent().getMessages();
      Set<String> groupStudy = redis.setfindAll("groupStudy");
      if (groupStudy.contains(groupid)){
          final int time = 30;
          event.replyAsync ("请问您要触发的关键词是什么?");
          message = new  Message();
          sessionContext.waitingForNextMessage(accountCode , GroupMessageEvent.Key, time, TimeUnit.SECONDS, (e, c) ->{
              if (!(c.getAuthor().getId().toString().equals(accountCode) && c.getGroup().getId().toString().equals(groupid ))) {
                  return false;
              }
              if (e instanceof TimeoutException) {
                  c.replyAsync("超时啦");
              }
              Messages messages1 = c.getMessageContent().getMessages();
              for (love.forte.simbot.message.Message.Element<?> element :messages1) {
                  if(element instanceof Text text){
                      message.setKeymessage(text.getText());
                  }
              }
              return true;
          });
          event.replyAsync ( "请继续输入触发关键词之后要返回什么 (目前支持返回文字和图片):");
          StringBuilder sendvalue=new StringBuilder();
          StringBuilder sendurl=new StringBuilder();

          sessionContext.waitingForNextMessage(accountCode , GroupMessageEvent.Key, time, TimeUnit.SECONDS, (e, c) ->{
              if (!(c.getAuthor().getId().toString().equals(accountCode) && c.getGroup().getId().toString().equals(groupid ))) {
                  return false;
              }
              if (e instanceof TimeoutException) {
                  c.replyAsync("超时啦");
              }
              message.setQq(accountCode);
              Messages messages1 = c.getMessageContent().getMessages();
              for (love.forte.simbot.message.Message.Element<?> element :messages1) {
                  if(element instanceof Text text){
                      sendvalue.append(text.getText());
                      //        message.setValuemessage(text.getText());
                  }
                  if (element instanceof Image<?> image) {
                      String url = image.getResource().getName();
                      String s = null;
                      try {
                          s = send_to_minio.Send_ToMinio_Picture_new(url);
                          String s1 = MakeNeko.MakePicture(s);
                          sendurl.append(s+"\n");
                          sendvalue.append(s1);
                      } catch (IOException ex) {
                          ex.printStackTrace();
                      }
                  }
              }
              message.setUrl( sendurl.toString());
              message.setValuemessage(sendvalue.toString());
              return true;
          });
          if (message.getValuemessage()!=null){
              int study = service.InsertMessage(message);
              if (study <1){
                  event.replyAsync ( "学习失败");
              }else{
                  event.replyAsync ( "学习成功");
              }

          }else{
              event.replyAsync ( "学习失败");
          }
      }else {
          event.replyAsync ( "管理员未在该群中开启学习功能");
      }





    }







    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Set<String> groupReply = redis.setfindAll("groupReply");
        for (String s : groupReply) {
            this.groupReply.add(Integer.valueOf(s) );
        }
    }
}
