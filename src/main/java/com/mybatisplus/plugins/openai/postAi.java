package com.mybatisplus.plugins.openai;

import com.google.gson.Gson;
import com.mybatisplus.plugins.openai.data.openAiData;
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

    @Listener
    @Filter(value = "/q ", matchType = MatchType.TEXT_STARTS_WITH)
    public void openAi(GroupMessageEvent event) throws Exception {
        if (true) {
            Gson gson = new Gson();
            Map<String, Object> params = new HashMap<>();
            params.put("model", "text-davinci-003");
            params.put("prompt", new Scanner(event.getMessageContent().getPlainText().substring(3)).next());
            params.put("max_tokens", 4000);
            log.info("触发");

            String post = openai_api.getPost("https://api.openai.com/v1/completions", gson.toJson(params));
            openAiData openAiData = gson.fromJson(post, openAiData.class);
            for (int i = 0; i < openAiData.getChoices().size(); i++) {
                event.replyAsync(openAiData.getChoices().get(i).getText());
                log.info(openAiData.getChoices().get(i).getText());
            }

        }
    }
}
