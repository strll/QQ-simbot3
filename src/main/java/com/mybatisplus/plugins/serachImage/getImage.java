package com.mybatisplus.plugins.serachImage;

import com.google.gson.Gson;
import com.mybatisplus.utils.OK3HttpClient;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Image;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;

import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 *
 * <p> @author mirai
 * <p> @className  getImage
 * <p> @data  2023/01/15 18:01
 * <p> @description
 */

@Slf4j
@Component
public class getImage {

    @Listener
    @Filter(value = "搜图",matchType = MatchType.TEXT_STARTS_WITH)
    public void searchImage(GroupMessageEvent event){

        var params = new HashMap<String,Object>();
        params.put("db",999);
        params.put("output_type",2);
        params.put("testmode",1);
        params.put("numres",16);
        params.put("api_key","");


        var url="https://saucenao.com/search.php";

        var header = new HashMap<String, String>();
        header.put("cookie", "_ga=GA1.1.678918584.1673528436; cf_clearance=yzbMlU38VCL3ShOlAoFs064Y5wChSPRUZmdEt14TlME-1673754810-0-150; token=63c378ba6ab35; user=69680; auth=1f1c47f43b0ae73c920f85075aa23cdbd5efee86; _ga_LK5LRE77R3=GS1.1.1673773844.5.0.1673773844.0.0.0");
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        var messagesBuilder = new MessagesBuilder();

        for (val message : event.getMessageContent().getMessages()) {
            if (message instanceof Image<?> image) {
                params.put("url",image.getResource().getName());
                String httpImage = OK3HttpClient.httpGet(url, params, header);
                log.info(httpImage);
                data data = new Gson().fromJson(httpImage, data.class);
                messagesBuilder.at(event.getAuthor().getId());
                try {
                    data.getResults().forEach(a->{
                        double similarity = 0;
                        try {
                            similarity = Double.parseDouble(a.getHeader().getSimilarity().trim());
                        } catch (NumberFormatException e) {
                            log.error(e.getMessage());
                        }
                        if (similarity>=60){
                            messagesBuilder.text("\n置信度: " + similarity + "\n").text("标题: " + a.getData().getTitle() + "\n");
                            messagesBuilder.text("PID: " + a.getData().getPixivId() + "\n").text("作者: " + a.getData().getMemberName() + "\n");
                            messagesBuilder.text("作者ID: "+a.getData().getMemberId()+"\n");
                            try {
                                messagesBuilder.image(Resource.of(new URL(a.getHeader().getThumbnail().trim())));
                            } catch (MalformedURLException e) {
                                log.error("无缩略图异常: "+e.getMessage());
                            }
                            if (a.getData().getExtUrls()!=null){
                                var imageUrl= String.valueOf(a.getData().getExtUrls());
                                messagesBuilder.text("图片链接: "+imageUrl+"\n");
                            }
                        }
                    });
                    event.getSource().sendBlocking(messagesBuilder.build());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    event.getSource().sendBlocking("搜索超出每日限制: 100 -> "+e.getMessage());
                }
            }
        }
    }
}
