package com.mybatisplus.controller;


import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.net.URL;

@Controller
public class GroupPictureController {


    @Async
    @Listener
    @Filter(value="nana图片",matchType = MatchType.TEXT_EQUALS)
    public void send_picture(GroupMessageEvent event) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet("https://www.dmoe.cc/random.php");
        try {

            //将HttpContext对象作为参数传给execute()方法,则HttpClient会把请求响应交互过程中的状态信息存储在HttpContext中
            HttpResponse response = httpClient.execute(httpGet, httpContext);
            //获取重定向之后的主机地址信息,即"http://127.0.0.1:8088"
            HttpHost targetHost = (HttpHost)httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            //获取实际的请求对象的URI,即重定向之后的"/blog/admin/login.jsp"
            HttpUriRequest realRequest = (HttpUriRequest)httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
            HttpEntity entity = response.getEntity();
            //String url = "[CAT:image,file=" + targetHost+realRequest.getURI() + "]";
            var  msg = new MessagesBuilder();
            msg.image(Resource.of(new URL( targetHost+""+realRequest.getURI())));
           event.getSource().sendAsync(msg.build());

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            httpClient.getConnectionManager().shutdown();
        }


    }
}
