package com.mybatisplus.controller;

import com.mybatisplus.config.minio.config.service.impl.MinIOFileStorageService;

import com.mybatisplus.entity.Today_Eat;
import com.mybatisplus.service.IAdminService;
import com.mybatisplus.service.TodayEatService;


import com.mybatisplus.utils.MakeNeko;
import com.mybatisplus.utils.MyRedis;
import com.mybatisplus.utils.SendMsgUtil;
import com.mybatisplus.utils.Send_To_minio;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.ID;
import love.forte.simbot.component.mirai.message.MiraiForwardMessage;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.component.mirai.message.SimbotOriginalMiraiMessage;
import love.forte.simbot.definition.GroupMember;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.*;
import love.forte.simbot.resources.Resource;
import love.forte.simbot.resources.URLResource;
import net.mamoe.mirai.internal.deps.io.ktor.client.plugins.ClientRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.EOFException;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.math.RandomUtils.nextDouble;

@Slf4j
@Controller
@Transactional
public class Group_Eat_Today implements ApplicationRunner {
    private HashSet<Integer> groupReply=new HashSet();

    @Autowired
    private MyRedis redis;

    @Autowired
    private Send_To_minio send_to_minio;




    @Autowired
    private TodayEatService todayEatService;

    private List<Today_Eat> today_eat=null;
    @Autowired
    private IAdminService adminService;


    @Async
    @Listener
    @Filter(value="nana????????????????????????",matchType = MatchType.TEXT_EQUALS)
    public void sendNews(GroupMessageEvent event) throws IOException {

        ID accountCode = event.getAuthor().getId();  //??????????????????QQ???
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode.toString()).getPermission()) <= 2) {
             redis.setAdd("todayEat", String.valueOf(event.getGroup().getId()));
            boolean add = groupReply.add(Integer.valueOf(String.valueOf(event.getGroup().getId())));
            if (add) {
                event.replyAsync("????????????");
            } else {
                event.replyAsync( "???????????????");
            }
        }
    }

    @Async
    @Listener
    @Filter(value="nana????????????????????????",matchType = MatchType.TEXT_EQUALS)
    public void removesend(GroupMessageEvent event) throws IOException {

        String accountCode = String.valueOf(event.getAuthor().getId());  //??????????????????QQ???
        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {

            redis.setDelete("todayEat",String.valueOf(event.getGroup().getId()));
            boolean add = groupReply.remove(Integer.valueOf(String.valueOf(event.getGroup().getId())));

            if (add) {
                event.replyAsync("????????????");
            } else {
                event.replyAsync("????????????");
            }
        }
    }


    @Filter(value = "nana???????????????",matchType = MatchType.TEXT_EQUALS)
    @Async
    @Listener
    public void sendeat(GroupMessageEvent event) throws IOException, EOFException {
        if (groupReply.contains(Integer.valueOf(String.valueOf(event.getGroup().getId())))) {
            if (today_eat == null) {
                this.today_eat = todayEatService.Send_Today_Eat_Message();
            }
            int size = today_eat.size();
            double v = nextDouble();
            double v1 = size * v;
            Today_Eat today_eat1 = today_eat.get((int) v1);
            String qq = today_eat1.getQq();
            String message = today_eat1.getMessage();
            try{
                try{
                    MessagesBuilder messagesBuilder = new MessagesBuilder();
                    String pattern = "https?://[^\\s]+";
                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(message);
                    String[] split = message.split("\n");
                    String messagetext= split[0];
                    String image ="";
                    if(m.find()){
                        String group = m.group();
                         image = group.replaceAll("]", "");
                    }

                    messagesBuilder.text("????????????" + qq + "?????????: \n+"+messagetext+"\n");
                    messagesBuilder.image(Resource.of(new URL(image)));
                    Messages build = messagesBuilder.build();
                //    event.replyAsync(build);
                    event.getSource().sendBlocking(build);

                }
                catch(IllegalStateException e){
                    event.replyAsync( "???????????????????????????????????????????????????????????????????????????????????? ??????id???"+today_eat1.getId());
                }
            }catch(ClientRequestException e) {
                String[] split = message.split("\n");
                event.replyAsync("???????????????????????? ?????????key???: "+split[0]+" ?????????????????????????????????");
                int i1 = todayEatService.select_Id_By_Msg(message);
                int i = todayEatService.Delete_Today_Eat_Message(i1);
                if (i !=0){
                    event.replyAsync( "????????????????????????");
                }else{
                    event.replyAsync("????????????????????????");
                }

            }

        } else{
            event.replyAsync("???????????????????????????");
        }

    }

  @Listener
    @Filter(value = "?????????????????????", matchType = MatchType.TEXT_STARTS_WITH)
    @Async
    public void studyeat(GroupMessageEvent event) throws IOException {

     // log.info(event.getMessageContent().getPlainText());
      if( event.getMessageContent().getPlainText().length()==7){
          event.replyAsync( "????????????????????? ?????????????????????????????????????????????nana?????? ????????????????????????????????????");
        }else {
          Today_Eat eat = new Today_Eat();
          ID id = event.getAuthor().getId();
          String qq=id.toString();
          String nickname=event.getAuthor().getNickOrUsername();

                eat.setQq(nickname + "(" + qq + ")");
          if (groupReply.contains(Integer.valueOf(String.valueOf(event.getGroup().getId())))) {
               // try{
                    String s="";
                    for (Message.Element<?> message : event.getMessageContent().getMessages()) {
                        if (message instanceof Image<?> image) {
                        //    log.info(MessageFormat.format("[????????????: {0} ]", image.getResource().getName()));

                            String fileId= send_to_minio.Send_ToMinio_Picture_new(image.getResource().getName());
                            s =s+ MakeNeko.MakePicture(fileId);
                        }
                        if (message instanceof Text) {
                       //     log.info(MessageFormat.format("[????????????: {0} ]", ((Text) message).getText().substring(7)));
                            s =s+((Text) message).getText().substring(7)+"\n";
                        }
                    }
              eat.setMessage( "\n" +s);

              int i = todayEatService.Studay_Today_Eat_Message(eat);
                if (i != 0) {
                    event.replyAsync ( "?????? ??????????????? ??????");
                    this.today_eat = null;
                    this.today_eat = todayEatService.Send_Today_Eat_Message();
                } else {
                    event.replyAsync ( "?????? ??????????????? ??????");
                }
            }else {
               event.getSource().sendAsync("???????????????????????????");
          }
        }
    }



  @Listener
    @Filter(value = "?????????????????????", matchType = MatchType.TEXT_STARTS_WITH)
    @Async
    public void selecteat(GroupMessageEvent event) throws IOException {
        String text =event.getMessageContent().getPlainText().substring(8);
        List<Today_Eat> list  =todayEatService.selectMsg(text);
      MiraiForwardMessageBuilder miraiForwardMessageBuilder=new MiraiForwardMessageBuilder();

      var messagesBuilder = new MessagesBuilder();

      for (Today_Eat end :list) {
          String message = end.getMessage();

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
    messagesBuilder.append("???????????????ID???: "+end.getId()+"\n???????????????:\n"+text1);
    messagesBuilder.image(Resource.of(new URL(s)));
    messagesBuilder.append("\n????????????:\n"+end.getQq());
    miraiForwardMessageBuilder.add(event.getBot().getId(),event.getBot().getUsername(),messagesBuilder.build());
      }


      event.replyAsync(miraiForwardMessageBuilder.build());



    }


    @Filter(value = "?????????????????????", matchType = MatchType.TEXT_STARTS_WITH)
    @Async
    @Listener
    public void deleteeat(GroupMessageEvent event) throws IOException {
        ID id = event.getGroup().getId();
        if (groupReply.contains(Integer.valueOf( id+"")) ){
            String text =event.getMessageContent().getPlainText().substring(7);
            int i = 0;
            try {
               Today_Eat today_Eat = todayEatService.select_Todayeat_By_id(Integer.parseInt(text.replaceAll(" ","")));
                    String message = today_Eat.getMessage();

                    if(message.contains("file=")){
                        String[] split = message.split("file=");
                        String substring = split[1].replace("]", "");
                        send_to_minio.Send_To_minio_Delete(substring);
                    }
                int number=Integer.parseInt(text.replaceAll(" ",""));
                i = todayEatService.Delete_Today_Eat_Message(number);

            } catch (Exception e) {

               event.replyAsync("???????????? ????????? ????????????????????? ?????????????????????id");
            }
            if (i != 0) {
                this.today_eat = null;
                //????????????
                this.today_eat = todayEatService.Send_Today_Eat_Message();
                event.replyAsync( "????????????");
            } else {
                event.replyAsync( "????????????");
            }
        } else{
            event.replyAsync("???????????????????????????");
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Set<String> groupReply = redis.setfindAll("todayEat");
        for (String s : groupReply) {
            this.groupReply.add(Integer.valueOf(s));
        }
    }

}