package com.mybatisplus.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Random_say {
    public String say() throws IOException {
        Document document = Jsoup.connect("https://v.api.aa1.cn/api/api-wenan-wangyiyunreping/index.php?aa1=text").get();
        Elements body = document.select("body");
        return  body.text();
    }
}
