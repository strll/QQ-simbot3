package com.mybatisplus.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mybatisplus.entity.Muicpojo;
import com.mybatisplus.entity.music.musicData;
import com.mybatisplus.entity.music.musicJsonRootBean;
import com.mybatisplus.utils.GetMuic;

import com.mybatisplus.utils.OK3HttpClient;
import lombok.extern.log4j.Log4j2;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.component.mirai.message.MiraiMusicShare;

import love.forte.simbot.event.GroupMessageEvent;
import net.mamoe.mirai.message.data.MusicKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

@Controller
@Log4j2
public class GroupSong {


    /**
     * 点歌功能
     * */

    @Autowired
    private GetMuic getMuic;

//    @OnGroup
//    @Filter(value = "nana听歌", trim=true,matchType = MatchType.CONTAINS)
//    @Async
//    public void listenSong1(GroupMsg msg, MsgSender sender) throws IOException {
//        String text = msg.getText().substring(6);
//        String muic = getMuic.getMuic(text);
//        sender.SENDER.sendGroupMsg(msg,muic);
//    }

    @Listener
    @Filter(value = "nana点歌 {{text}}")
    public void sendMusic(GroupMessageEvent event, @FilterValue("text") String text){
        try {
            Muicpojo muicV3 = getMuic.getMuicV3(text);
            var miraiMusicShare = new MiraiMusicShare(MusicKind.NeteaseCloudMusic,  muicV3.getMusic(), muicV3.getNickUser(),muicV3.getMusicUrl(), muicV3.getPicture(), muicV3.getUrl());

            event.getSource().sendBlocking(miraiMusicShare);

        } catch (IOException e) {
            e.printStackTrace();
            event.replyAsync(MessageFormat.format("歌曲插件异常,疑似接口出现错误: {0}", e.getMessage()));
        }
//        var params= new HashMap<String,Object>();
//        params.put("msg",text.trim());
//        params.put("num",1);
//        params.put("n",1);
//        try {
//            String musicJson = OK3HttpClient.httpGet("https://www.dreamling.xyz/API/163/music/api.php", params, null);
//            log.info(musicJson);
//            musicJsonRootBean musicData = new Gson().fromJson(musicJson, musicJsonRootBean.class);
//            int code = musicData.getCode();
//            if (code==200){
//                var data = musicData.getData();
//                String getPicture = data.getCover();
//                String getMusic = data.getMusic();
//                String getNickUser = data.getSinger();
//                String getMusicUrl = data.getMusic_Url();
//                String getUrl = data.getUrl();
//                var miraiMusicShare = new MiraiMusicShare(MusicKind.NeteaseCloudMusic, getMusic, getNickUser,getMusicUrl, getPicture, getUrl);
//
//                event.getSource().sendBlocking(miraiMusicShare);
//            }else {
//                log.error("code异常.....");
//                event.replyAsync("code异常..： "+code);
//            }
//        } catch (JsonSyntaxException e) {
//            log.error(MessageFormat.format("歌曲插件异常,疑似接口出现错误: {0}", e.getMessage()));
//            event.replyAsync(MessageFormat.format("歌曲插件异常,疑似接口出现错误: {0}", e.getMessage()));
//        }
    }


}
