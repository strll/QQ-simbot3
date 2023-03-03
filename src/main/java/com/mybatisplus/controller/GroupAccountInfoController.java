package com.mybatisplus.controller;


import com.google.gson.Gson;
import com.mybatisplus.entity.bilil.pojo.JsonRootBean;
import com.mybatisplus.entity.bilil.pojo.Meta;
import com.mybatisplus.plugins.openai.data.openAiData;
import com.mybatisplus.utils.*;
import love.forte.simbot.component.mirai.message.MiraiForwardMessage;
import love.forte.simbot.component.mirai.message.SimbotOriginalMiraiMessage;
import love.forte.simbot.message.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mybatisplus.entity.Message;
import com.mybatisplus.service.IAdminService;
import com.mybatisplus.service.IMessageService;
import com.mybatisplus.service.impl.MessageServiceImpl;

import com.mybatisplus.utils.Qiniuyun.util.Get_Url_in_Text_and_get_Picture_from_Url;
import lombok.extern.log4j.Log4j2;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.ID;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.definition.GroupMember;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.resources.Resource;
import net.mamoe.mirai.message.data.RichMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;


@Log4j2
@Transactional
@Controller
public class GroupAccountInfoController implements ApplicationRunner {
    @Autowired
    private IMessageService service;


    private HashSet<Integer> groupReply = new HashSet();
    private HashSet<Long> blackset = new HashSet();

    @Autowired
    private IAdminService adminService;
    @Autowired
    private Random_say random_say;

    @Autowired
    private MyRedis redis;


    @Autowired
    private Get_Url_in_Text_and_get_Picture_from_Url get_url_in_text_and_get_picture_from_url;

    @Async
    @Listener
    @Filter(value = "nana帮助", matchType = MatchType.TEXT_EQUALS)
    public void help(GroupMessageEvent event) {

        MiraiForwardMessageBuilder miraiForwardMessageBuilder = new MiraiForwardMessageBuilder();
        var messagesBuilder = new MessagesBuilder();
        messagesBuilder.append("目前nana有一下几个功能:" +
                "\n 1.关键词触发" +
                "\n 2.学习功能 发送 nana学习 可以触发 " +
                "\n 学习第一次发送的是key要求key必须是字符(建议出发关键字不要过短因为查询用的是模糊查询) " +
                "\n 第二次发送的是value value可以是图片 " +
                "\n 3.删除功能 发送nana删除 可以触发 " +
                "\n 4.nana图片 发送随机二次元图片(2022.8.1新增) " +
                "\n 6.定时发送固定信息(2022.8.3新增)" +
                "\n 7.新增权限管理(2022.8.3新增)" +
                "\n 如果机器人出现bug请管理员及时禁言 " +
                "\n 8.nana天气 新增天气查询(2022.8.15)" +
                "\n 9.nana模块管理" +
                "\n 10.nana点歌(示例:nana点歌Hurt)" +
                "\n 11.nana每日新闻" +
                "\n 12.nana微博热搜" +
                "\n 13.nana历史上的今天"
                + "\n 15.nana翻译(示例: nana翻译 hello)"
                + "\n 16.nana百度(使用方法示例: nana百度 春节) "
                + "\n 17.nana查询 使用方法同上(改接口失效请使用百度功能进行搜索)"
                + "\n 18.nana摸鱼日历" +
                "\n 19.nana找番(用法: nana找番+动漫截图)"
                + "\n 20.nana聊天(nana监听你发送的信息并且做出回复 退出请输入nana退出聊天)"
                + "\n 21.nana今天吃什么(或者使用 饿了) 随机发送\n"
                + "\n  学习今天吃什么 (示例: 学习今天吃什么 泡面 泡面图片)"
                + "\n  删除今天吃什么 id  id请输入 查看所有今天吃什么 获取所有id"
                + "\n  查询今天吃什么空格+关键词 可通过模糊查询获取内容的所有信息 可通过id进行删除"
                + "\n 22.nana动漫资讯"
                + "\n 23.nana小鸡词典 (示例: nana小鸡词典 盐系) 微博恶意盗取小鸡词典文库 小鸡词典在于微博的官司中入不敷出 现网站服务器因为某些特殊原因永久关闭 "
                + "\n 24.nana刷新"
                + "\n 25.回复功能中新增图片解析 可以根据发送的链接访问并且生产图片 (效率可能较低 2023.1.26新增)" +
                "\n 26.nana搜图 (2023.2.9新增)" +
                "\n 27./q 访问gpt(示例 /q 你好)"
        );
        miraiForwardMessageBuilder.add(event.getBot().getId(), event.getBot().getUsername(), messagesBuilder.build());
        event.getSource().sendBlocking(miraiForwardMessageBuilder.build());
    }

    @Autowired
    private MessageServiceImpl messageServiceImpl;

    @Async
    @Listener
    @Filter(value = "nana刷新", matchType = MatchType.TEXT_EQUALS)
    public void help1(GroupMessageEvent event) {
        int i = messageServiceImpl.Delete_Null_Message();
        event.getSource().sendBlocking("已刷新 删除无效数据:" + i + "条");
    }

    @Listener
    @Filter(value = "nana模块管理", matchType = MatchType.TEXT_EQUALS)
    public void helpmk(GroupMessageEvent event) {

        MiraiForwardMessageBuilder miraiForwardMessageBuilder = new MiraiForwardMessageBuilder();
        var messagesBuilder = new MessagesBuilder();
        messagesBuilder.append("nana启动学习模块\n" +
                "nana关闭学习模块\n " +
                "nana关闭定时模块\n" +
                "nana启动定时模块\n" +
                "nana关闭天气模块\n"
                + "nana添加群定时\n"
                + "nana取消群定时\n"
                + "nana添加群回复 \n"
                + "nana取消群回复\n"
                + "nana开启聊天模块 (这个模块是nana聊天) \n"
                + "nana关闭聊天模块\n"
                + "nana添加群今天吃什么\n"
                + "nana取消群今天吃什么 \n"
                + "nana添加黑名单 @\n"
                + "nana取消黑名单 @\n"
        );
        miraiForwardMessageBuilder.add(event.getBot().getId(), event.getBot().getUsername(), messagesBuilder.build());
        event.getSource().sendBlocking(miraiForwardMessageBuilder.build());

    }


    @org.springframework.scheduling.annotation.Async
    @Listener
    @Filter(value = "nana添加黑名单", matchType = MatchType.TEXT_EQUALS)
    public void black(GroupMessageEvent event) throws IOException {
        String id = event.getAuthor().getId().toString();  //获取发送人的QQ号
        var group = event.getGroup();
        if (Integer.parseInt(adminService.get_Admin_permission(id).getPermission()) <= 2) {
            for (love.forte.simbot.message.Message.Element<?> message : event.getMessageContent().getMessages()) {
                if (message instanceof At) {
                    ID targetId = ((At) message).getTarget();
                    GroupMember targetMember = group.getMember(targetId);
                    if (targetMember == null) {
                        log.info(MessageFormat.format("[AT消息:未找到目标用户: {0} ]", targetId));
                    } else {
                        redis.setAdd("black", targetId.toString());
                        blackset.add(Long.parseLong(targetId.toString()));
                    }
                }
            }

        }
        StringBuilder sb = new StringBuilder();
        for (Long integer : blackset) {
            sb.append(integer + "\n");
        }
       event.getSource().sendBlocking("黑名单中已存在的QQ号有:\n" + sb);
    }


    @org.springframework.scheduling.annotation.Async
    @Listener
    @Filter(value = "nana取消黑名单", matchType = MatchType.TEXT_EQUALS)
    public void blackdelect(GroupMessageEvent event) throws IOException {
        String id = event.getAuthor().getId().toString();  //获取发送人的QQ号
        var group = event.getGroup();
        if (Integer.parseInt(adminService.get_Admin_permission(id).getPermission()) <= 2) {

            for (love.forte.simbot.message.Message.Element<?> message : event.getMessageContent().getMessages()) {
                if (message instanceof At) {
                    ID targetId = ((At) message).getTarget();
                    GroupMember targetMember = group.getMember(targetId);
                    if (targetMember == null) {
                        log.info(MessageFormat.format("[AT消息:未找到目标用户: {0} ]", targetId));
                    } else {
                        redis.setDelete("black", targetId.toString());
                        blackset.remove(Long.parseLong(targetId.toString()));
                    }
                }
            }


        }
        StringBuilder sb = new StringBuilder();
        for (Long integer : blackset) {
            sb.append(integer + "\n");

        }
       event.getSource().sendBlocking("黑名单中已存在的QQ号有:\n" + sb);
    }


    //==================================
    @org.springframework.scheduling.annotation.Async
    @Listener
    @Filter(value = "nana添加群回复", matchType = MatchType.TEXT_EQUALS)
    public void sendNews(GroupMessageEvent event) throws IOException {
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号
        var group = event.getGroup().getId();


        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {

            //使用redis处理是否群回复
            redis.setAdd("groupReply", group.toString());
            boolean add = groupReply.add(Integer.valueOf(group.toString()));


            if (add) {
               event.getSource().sendBlocking("添加成功");
            } else {
               event.getSource().sendBlocking("已存在该群");
            }
        }
    }

    //==================================
    @org.springframework.scheduling.annotation.Async
    @Listener
    @Filter(value = "nana取消群回复", matchType = MatchType.TEXT_EQUALS)
    public void removesend(GroupMessageEvent event) throws IOException {
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号

        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {

            //改用redis
            redis.setDelete("groupReply", event.getGroup().getId().toString());
            boolean add = groupReply.remove(Integer.valueOf(event.getGroup().getId().toString()));


            if (add) {
               event.getSource().sendBlocking("取消成功");
            } else {
               event.getSource().sendBlocking("取消失败");
            }
        }
    }

    //====================================================

    @Autowired
    private Get_Picture_in_Text get_picture;

    @Async
    @Listener
    public void onGroupMsg(GroupMessageEvent event) throws IOException {
        var group = event.getGroup();
        String accountCode = event.getAuthor().getId().toString();  //获取发送人的QQ号

        if (groupReply.contains(Integer.valueOf(event.getGroup().getId().toString())) && !blackset.contains(Long.parseLong(accountCode))) {
            String valuemessage = "";
            for (love.forte.simbot.message.Message.Element<?> message : event.getMessageContent().getMessages()) {
//                if (message instanceof Image<?> image) {
//                    String picture = get_url_in_text_and_get_picture_from_url.getPicture(image.getResource().getName());
//                    if (picture != null) {
//                        SendMsgUtil.sendSimpleGroupImage(event.getGroup(), picture);
//                    }
//                }
                if (message instanceof Text text) {
                    String text1 = ((Text) message).getText();
                    //校验text中是否含有url 如果有url的话解析并发送图片
                    try {
                        MessagesBuilder messagesBuilder1 = get_picture.get(text1);
                        event.getSource().sendBlocking(messagesBuilder1.build());
                    }catch (Exception e){

                    }

                    //

                    Message message1 = new Message();
                    message1.setKeymessage(text1);
                    List<Message> messages = service.Get_Message_by_key(message1);
                    int size = messages.size();
                    if (messages.size() != 0) {
                        if (messages.size() == 1) {
                            valuemessage = messages.get(0).getValuemessage();
                            if (valuemessage.equals("")) {
                                MessagesBuilder messagesBuilder = new MessagesBuilder();
                                messagesBuilder.image(Resource.of(new URL(messages.get(0).getUrl())));
                                Messages build = messagesBuilder.build();
                               event.getSource().sendBlocking(build);
                            } else {
                                String v = messages.get(0).getValuemessage();
                                MessagesBuilder message2 = Cat_to_message.getMessage(messages.get(0).getValuemessage());
                               event.getSource().sendBlocking(message2.build());
                            }

                        } else {
                            double d = Math.random();
                            int i = (int) (d * size);
                            valuemessage = messages.get(i).getValuemessage();
                            if (valuemessage.equals("")) {
                                MessagesBuilder messagesBuilder = new MessagesBuilder();
                                try {
                                    messagesBuilder.image(Resource.of(new URL(messages.get(i).getUrl())));
                                    }catch (Exception e){

                                }

                                Messages build = messagesBuilder.build();
                               event.getSource().sendBlocking(build);

                            } else {
                                String v = messages.get(i).getValuemessage();
                                MessagesBuilder message2 = Cat_to_message.getMessage(v);
                               event.getSource().sendBlocking(message2.build());
                            }
                        }
                    }


                }
                if (message instanceof SimbotOriginalMiraiMessage) {

                        // 获取信息
                        String s = ((SimbotOriginalMiraiMessage) message).getOriginalMiraiMessage().contentToString();
                    JSONObject jsonObject = JSON.parseObject(s);
                    if(!Objects.isNull(jsonObject.get("desc").toString())){
                        String desc = jsonObject.get("desc").toString();
                        if (desc!=null&&desc.equals("哔哩哔哩")){

                            String meta = jsonObject.get("meta").toString();
                            JSONObject json = JSON.parseObject(meta);
                            JSONObject  detail_1= (JSONObject) json.get("detail_1");
                            String url =  detail_1.get("qqdocurl").toString();

                            Document parse = null;
                            try {
                                parse = Jsoup.parse(new URL(url), 3000);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Elements elementsByClass = parse.getElementsByClass("desc-info desc-v2 open");
                            Element comment = parse.getElementById("v_desc");
                            event.getGroup().sendBlocking("视频的简介是:\n"+comment.text());
                        }
                    }


                }
                if (message instanceof At) {
                    ID targetId = ((At) message).getTarget();
                    if(targetId.toString().equals(event.getBot().getId().toString()))
                    {
                        GroupMember targetMember = group.getMember(targetId);
                        if (targetMember == null) {
                            //             log.info(MessageFormat.format("[AT消息:未找到目标用户: {0} ]", targetId));
                        } else {
                            //               log.info(MessageFormat.format("[AT消息: @{0}( {1} )", targetMember.getNickOrUsername(), targetMember.getId()));
                            event.getSource().sendBlocking(random_say.say() );

                        }


                        //  String say = random_say.say();
                        if (message instanceof MiraiForwardMessage miraiForwardMessage) {
                            miraiForwardMessage.getOriginalForwardMessage().getNodeList().forEach(a -> {
                                log.info(MessageFormat.format("[转发消息: \n内容: {0} ]", a.getMessageChain()));
                            });
                        }
                    }


                }
            }


            if ((int) Math.round(Math.random() * 10000) < 7) {
                //随机复读
                event.getSource().sendBlocking(event.getMessageContent());
            }



        }
    }

        @Override
        public void run (ApplicationArguments args) throws Exception {
            Set<String> groupReply = redis.setfindAll("groupReply");
            Set<String> black = redis.setfindAll("black");
            for (String s : groupReply) {
                this.groupReply.add(Integer.valueOf(s));
            }
            for (String s : black) {
                this.blackset.add(Long.parseLong(s));
            }
        }
    }

