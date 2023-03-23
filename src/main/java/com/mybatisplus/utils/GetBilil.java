package com.mybatisplus.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GetBilil {
    public String get(String url ) throws IOException {

        String re="";
        Connection.Response execute = Jsoup.connect(url)
                .ignoreContentType(true)
                .execute();
        org.jsoup.nodes.Document parse = execute.parse();
        Elements select = parse.select("#v_desc > div.desc-info.desc-v2 > span");
        for (org.jsoup.nodes.Element element : select) {

            re=re+element.text();
        }

        return re;
    }
}
