package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONObject;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class GetMoYu {

    public MessagesBuilder getMoyu() {
        var messagesBuilder = new MessagesBuilder();
        try {


            Connection.Response host = Jsoup.connect("https://api.emoao.com/api/moyu?type=json")
                    .header("Content-Type","application/json")
                    .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                    .header("Accept", "text/html, application/xhtml+xml, */*")
                    .ignoreContentType(true)
                    .execute();


            String body = host.body();
            JSONObject jsonObject = JSONObject.parseObject(body);
            String string = jsonObject.getString("title");
            String string1 = jsonObject.getString("imgurl");
            messagesBuilder.append(string).image(Resource.of(new URL(string1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    return messagesBuilder;

//        HttpClient httpClient = new DefaultHttpClient();
//        HttpContext httpContext = new BasicHttpContext();
//        HttpGet httpGet = new HttpGet("https://api.emoao.com/api/moyu");
//        try {
//            //将HttpContext对象作为参数传给execute()方法,则HttpClient会把请求响应交互过程中的状态信息存储在HttpContext中
//            HttpResponse response = httpClient.execute(httpGet, httpContext);
//            //获取重定向之后的主机地址信息
//            HttpHost targetHost = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
//            //获取实际的请求对象的URI,即重定向之后的地址
//            HttpUriRequest realRequest = (HttpUriRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
//            HttpEntity entity = response.getEntity();
//        //    String url = "[CAT:image,file=" + targetHost + realRequest.getURI() + "]";
//            String url = targetHost+"" + realRequest.getURI() ;
//
//            return url;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            httpClient.getConnectionManager().shutdown();
//        }
//        return "获取摸鱼日历失败";
    }
}

