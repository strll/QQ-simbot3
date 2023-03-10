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
    @Filter(value = "nana??????", matchType = MatchType.TEXT_EQUALS)
    public void help(GroupMessageEvent event) {

        MiraiForwardMessageBuilder miraiForwardMessageBuilder = new MiraiForwardMessageBuilder();
        var messagesBuilder = new MessagesBuilder();
        messagesBuilder.append("??????nana?????????????????????:" +
                "\n 1.???????????????" +
                "\n 2.???????????? ?????? nana?????? ???????????? " +
                "\n ???????????????????????????key??????key???????????????(??????????????????????????????????????????????????????????????????) " +
                "\n ?????????????????????value value??????????????? " +
                "\n 3.???????????? ??????nana?????? ???????????? " +
                "\n 4.nana?????? ???????????????????????????(2022.8.1??????) " +
                "\n 6.????????????????????????(2022.8.3??????)" +
                "\n 7.??????????????????(2022.8.3??????)" +
                "\n ?????????????????????bug???????????????????????? " +
                "\n 8.nana?????? ??????????????????(2022.8.15)" +
                "\n 9.nana????????????" +
                "\n 10.nana??????(??????:nana??????Hurt)" +
                "\n 11.nana????????????" +
                "\n 12.nana????????????" +
                "\n 13.nana??????????????????"
                + "\n 15.nana??????(??????: nana?????? hello)"
                + "\n 16.nana??????(??????????????????: nana?????? ??????) "
                + "\n 17.nana?????? ??????????????????(????????????????????????????????????????????????)"
                + "\n 18.nana????????????" +
                "\n 19.nana??????(??????: nana??????+????????????)"
                + "\n 20.nana??????(nana?????????????????????????????????????????? ???????????????nana????????????)"
                + "\n 21.nana???????????????(???????????? ??????) ????????????\n"
                + "\n  ????????????????????? (??????: ????????????????????? ?????? ????????????)"
                + "\n  ????????????????????? id  id????????? ??????????????????????????? ????????????id"
                + "\n  ???????????????????????????+????????? ???????????????????????????????????????????????? ?????????id????????????"
                + "\n 22.nana????????????"
                + "\n 23.nana???????????? (??????: nana???????????? ??????) ???????????????????????????????????? ???????????????????????????????????????????????? ?????????????????????????????????????????????????????? "
                + "\n 24.nana??????"
                + "\n 25.????????????????????????????????? ??????????????????????????????????????????????????? (?????????????????? 2023.1.26??????)" +
                "\n 26.nana?????? (2023.2.9??????)" +
                "\n 27./q ??????gpt(?????? /q ??????)"
        );
        miraiForwardMessageBuilder.add(event.getBot().getId(), event.getBot().getUsername(), messagesBuilder.build());
        event.getSource().sendBlocking(miraiForwardMessageBuilder.build());
    }

    @Autowired
    private MessageServiceImpl messageServiceImpl;

    @Async
    @Listener
    @Filter(value = "nana??????", matchType = MatchType.TEXT_EQUALS)
    public void help1(GroupMessageEvent event) {
        int i = messageServiceImpl.Delete_Null_Message();
        event.getSource().sendBlocking("????????? ??????????????????:" + i + "???");
    }

    @Listener
    @Filter(value = "nana????????????", matchType = MatchType.TEXT_EQUALS)
    public void helpmk(GroupMessageEvent event) {

        MiraiForwardMessageBuilder miraiForwardMessageBuilder = new MiraiForwardMessageBuilder();
        var messagesBuilder = new MessagesBuilder();
        messagesBuilder.append("nana??????????????????\n" +
                "nana??????????????????\n " +
                "nana??????????????????\n" +
                "nana??????????????????\n" +
                "nana??????????????????\n"
                + "nana???????????????\n"
                + "nana???????????????\n"
                + "nana??????????????? \n"
                + "nana???????????????\n"
                + "nana?????????????????? (???????????????nana??????) \n"
                + "nana??????????????????\n"
                + "nana????????????????????????\n"
                + "nana???????????????????????? \n"
                + "nana??????????????? @\n"
                + "nana??????????????? @\n"
        );
        miraiForwardMessageBuilder.add(event.getBot().getId(), event.getBot().getUsername(), messagesBuilder.build());
        event.getSource().sendBlocking(miraiForwardMessageBuilder.build());

    }


    @org.springframework.scheduling.annotation.Async
    @Listener
    @Filter(value = "nana???????????????", matchType = MatchType.TEXT_EQUALS)
    public void black(GroupMessageEvent event) throws IOException {
        String id = event.getAuthor().getId().toString();  //??????????????????QQ???
        var group = event.getGroup();
        if (Integer.parseInt(adminService.get_Admin_permission(id).getPermission()) <= 2) {
            for (love.forte.simbot.message.Message.Element<?> message : event.getMessageContent().getMessages()) {
                if (message instanceof At) {
                    ID targetId = ((At) message).getTarget();
                    GroupMember targetMember = group.getMember(targetId);
                    if (targetMember == null) {
                        log.info(MessageFormat.format("[AT??????:?????????????????????: {0} ]", targetId));
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
       event.getSource().sendBlocking("????????????????????????QQ??????:\n" + sb);
    }


    @org.springframework.scheduling.annotation.Async
    @Listener
    @Filter(value = "nana???????????????", matchType = MatchType.TEXT_EQUALS)
    public void blackdelect(GroupMessageEvent event) throws IOException {
        String id = event.getAuthor().getId().toString();  //??????????????????QQ???
        var group = event.getGroup();
        if (Integer.parseInt(adminService.get_Admin_permission(id).getPermission()) <= 2) {

            for (love.forte.simbot.message.Message.Element<?> message : event.getMessageContent().getMessages()) {
                if (message instanceof At) {
                    ID targetId = ((At) message).getTarget();
                    GroupMember targetMember = group.getMember(targetId);
                    if (targetMember == null) {
                        log.info(MessageFormat.format("[AT??????:?????????????????????: {0} ]", targetId));
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
       event.getSource().sendBlocking("????????????????????????QQ??????:\n" + sb);
    }


    //==================================
    @org.springframework.scheduling.annotation.Async
    @Listener
    @Filter(value = "nana???????????????", matchType = MatchType.TEXT_EQUALS)
    public void sendNews(GroupMessageEvent event) throws IOException {
        String accountCode = event.getAuthor().getId().toString();  //??????????????????QQ???
        var group = event.getGroup().getId();


        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {

            //??????redis?????????????????????
            redis.setAdd("groupReply", group.toString());
            boolean add = groupReply.add(Integer.valueOf(group.toString()));


            if (add) {
               event.getSource().sendBlocking("????????????");
            } else {
               event.getSource().sendBlocking("???????????????");
            }
        }
    }

    //==================================
    @org.springframework.scheduling.annotation.Async
    @Listener
    @Filter(value = "nana???????????????", matchType = MatchType.TEXT_EQUALS)
    public void removesend(GroupMessageEvent event) throws IOException {
        String accountCode = event.getAuthor().getId().toString();  //??????????????????QQ???

        if (Integer.parseInt(adminService.get_Admin_permission(accountCode).getPermission()) <= 2) {

            //??????redis
            redis.setDelete("groupReply", event.getGroup().getId().toString());
            boolean add = groupReply.remove(Integer.valueOf(event.getGroup().getId().toString()));


            if (add) {
               event.getSource().sendBlocking("????????????");
            } else {
               event.getSource().sendBlocking("????????????");
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
        String accountCode = event.getAuthor().getId().toString();  //??????????????????QQ???

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
                    //??????text???????????????url ?????????url???????????????????????????
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

                        // ????????????
                        String s = ((SimbotOriginalMiraiMessage) message).getOriginalMiraiMessage().contentToString();
                    JSONObject jsonObject = JSON.parseObject(s);
                    if(!Objects.isNull(jsonObject.get("desc").toString())){
                        String desc = jsonObject.get("desc").toString();
                        if (desc!=null&&desc.equals("????????????")){

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
                            event.getGroup().sendBlocking("??????????????????:\n"+comment.text());
                        }
                    }


                }
                if (message instanceof At) {
                    ID targetId = ((At) message).getTarget();
                    if(targetId.toString().equals(event.getBot().getId().toString()))
                    {
                        GroupMember targetMember = group.getMember(targetId);
                        if (targetMember == null) {
                            //             log.info(MessageFormat.format("[AT??????:?????????????????????: {0} ]", targetId));
                        } else {
                            //               log.info(MessageFormat.format("[AT??????: @{0}( {1} )", targetMember.getNickOrUsername(), targetMember.getId()));
                            event.getSource().sendBlocking(random_say.say() );

                        }


                        //  String say = random_say.say();
                        if (message instanceof MiraiForwardMessage miraiForwardMessage) {
                            miraiForwardMessage.getOriginalForwardMessage().getNodeList().forEach(a -> {
                                log.info(MessageFormat.format("[????????????: \n??????: {0} ]", a.getMessageChain()));
                            });
                        }
                    }


                }
            }


            if ((int) Math.round(Math.random() * 10000) < 7) {
                //????????????
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

