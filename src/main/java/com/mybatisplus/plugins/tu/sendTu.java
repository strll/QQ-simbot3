package com.mybatisplus.plugins.tu;

import com.mybatisplus.utils.HttpClient4Util;
import com.mybatisplus.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.component.mirai.message.MiraiReceivedNudge;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Message;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;

import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.Plugins.Tu
 * @date 2022/12/8 17:47
 */
@Slf4j
@Component
public class sendTu {
    //    @Listener
    public void sendMei(GroupMessageEvent event) {
        for (Message.Element<?> message : event.getMessageContent().getMessages()) {
            if (message instanceof MiraiReceivedNudge nudge) {
                if (nudge.getTarget().equals(event.getGroup().getBot().getId())) {
                    try {
                        MessagesBuilder messagesBuilder = new MessagesBuilder();
                        messagesBuilder.at(event.getAuthor().getId());
                        messagesBuilder.append("\n[随机一言]\n" + HttpClient4Util.getResponse4GetAsString("https://xiaobai.klizi.cn/API/other/yy.php", "utf-8") + "\n");
                        messagesBuilder.image(Resource.of(new URL(HttpUtils.sendGet("http://ap1.iw233.cn/api.php?sort=pc"))));
                        event.getSource().sendBlocking(messagesBuilder.build());
                    } catch (Exception e) {
                        log.info("报错: " + e.getMessage());
                    }
                }
            }
        }
    }
}
