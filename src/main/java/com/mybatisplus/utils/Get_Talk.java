package com.mybatisplus.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class Get_Talk {
    //http://apii.gq/api/xiaoai.php?msg=hello

    public String get_talk(String msg) throws IOException {
        String replace = msg.replace("nana", "小爱");
        Document document = Jsoup.connect("http://apii.gq/api/xiaoai.php?msg="+replace)
                .get();
        Elements body = document.select("body");
        return body.text().replace("小爱","nana") ;

    }
}
