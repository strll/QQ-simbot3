package com.mybatisplus.plugins.openai;

import com.google.gson.Gson;
import com.mybatisplus.plugins.openai.data.openAiData;
import com.mybatisplus.utils.GetChatGpt;
import com.mybatisplus.utils.HttpClient4Util;
import com.mybatisplus.utils.Msg;
import com.mybatisplus.utils.Openai_api;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.openai
 * @date 2023/1/4 16:40
 */

@Component
@Slf4j
public class postAi {


    public postAi() throws IOException {

    }

    @Autowired
    private Openai_api openai_api;

    @Autowired
    private GetChatGpt getChatGpt;

    @Listener
    @Filter(value = "/q ", matchType = MatchType.TEXT_STARTS_WITH)
    public void openAi(GroupMessageEvent event) throws Exception {
            String next = new Scanner(event.getMessageContent().getPlainText().substring(3)).next();
            event.replyAsync(getChatGpt.Get(next));
    }
}
