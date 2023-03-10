package com.mybatisplus.app.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mybatisplus.entity.Message;
import com.mybatisplus.entity.Muicpojo;
import com.mybatisplus.entity.Today_Eat;
import com.mybatisplus.entity.app.AppAccountEnum;
import com.mybatisplus.entity.app.ResponseResult;
import com.mybatisplus.entity.app.Today_Eat_app;
import com.mybatisplus.entity.music.musicData;
import com.mybatisplus.entity.music.musicJsonRootBean;
import com.mybatisplus.service.IMessageService;
import com.mybatisplus.service.TodayEatService;
import com.mybatisplus.utils.*;
import com.mybatisplus.utils.app.GetNowWeather_app;
import com.mybatisplus.utils.app.Get_Picture_in_Text_app;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.math.RandomUtils.nextDouble;

@Service
public class BotService {
    @Autowired
    private Get_Cartoon_News cartoon_news;

    @Autowired
    private TodayEatService todayEatService;
    private List<Today_Eat> today_eat=null;

    @Autowired
    private Get_Picture_in_Text_app getPicture;
    @Autowired
    private IMessageService service;
    @Autowired
    private GetBaiDu getBaiDu;
    @Autowired
    private HistoryTody historyTody;

    @Autowired
    private GetNews getNews;

    @Autowired
    private GetMoYu moyu;
    @Autowired
    private GetTranslation_Content getTranslation_Content;
    @Autowired
    private GetWbTop10 getWbTop10;
@Autowired
    private GetNowWeather_app getNowWeather_app;
@Autowired
private GetMuic getMuic;
@Autowired
    private Get_Talk getTalk;
    public ResponseResult GetNowWeather(String city){
        try {
            String s = getNowWeather_app.GetWeather(city);
            return ResponseResult.okResult(s);
        } catch (IOException e) {
            return ResponseResult.okResult("天气模块失败");
        }

    }

    public ResponseResult GetWbTop10(){
        String wbTop10 = getWbTop10.getWbTop10();
        String[] ends = wbTop10.split("end");
        return ResponseResult.okResult(ends);
    }

    public ResponseResult GetTranslation(String msg){
        try {
            String translation_content = getTranslation_Content.getTranslation_content(msg);
            return   ResponseResult.okResult(translation_content);
        } catch (IOException e) {
            return   ResponseResult.okResult("获取翻译失败");
        }

    }

    public ResponseResult GetSong(String song){
        try {

//            this.msg = ref.data.data.data.url,
//                    this.pic = ref.data.data.data.cover,
//                    console.log(this.pic),
//                    this.songer = ref.data.data.data.singer
            Muicpojo muicV3 = getMuic.getMuicV3(song);
            musicJsonRootBean musicJsonRootBean = new musicJsonRootBean();
            musicJsonRootBean.setCode(200);
            musicData musicData = new musicData();

            musicData.setUrl(muicV3.getUrl());
            musicData.setSinger(muicV3.getNickUser());
            musicData.setCover(muicV3.getPicture()); //图片
            musicJsonRootBean.setData(musicData);
            return ResponseResult.okResult( musicJsonRootBean);
        } catch (IOException e) {
            e.printStackTrace();
            return   ResponseResult.okResult("歌曲插件异常,疑似接口出现错误");
        }

//        var params= new HashMap<String,Object>();
//        params.put("msg",song);
//        params.put("num",1);
//        params.put("n",1);
//        try {
//            String musicJson = OK3HttpClient.httpGet("https://www.dreamling.xyz/API/163/music/api.php", params, null);
//
//            musicJsonRootBean musicData = new Gson().fromJson(musicJson, musicJsonRootBean.class);
//            int code = musicData.getCode();
//            if (code==200){
//                var data = musicData.getData();
//                return   ResponseResult.okResult(musicData);
//            }else {
//                return   ResponseResult.okResult("获取歌曲失败");
//            }
//        } catch (JsonSyntaxException e) {
//            return   ResponseResult.okResult("歌曲插件异常,疑似接口出现错误");
//
//        }


    }

    public ResponseResult GetNews(){
        try {
            ArrayList<String> strings = getNews.EveryDayNews();
            strings.remove("");
            strings.remove("");
            return    ResponseResult.okResult( strings );

        } catch (IOException e) {
            e.printStackTrace();
            return  ResponseResult.okResult("获取新闻失败");
        }

    }

    public ResponseResult GetMoYu(){
        AppAccountEnum appAccountEnum = new AppAccountEnum();
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
            appAccountEnum.setMsg(string);
            appAccountEnum.setUrl(string1);
            return   ResponseResult.okResult( appAccountEnum);
        } catch (IOException e) {
            e.printStackTrace();
            AppAccountEnum appAccountEnum1 = new AppAccountEnum();
            appAccountEnum1.setMsg("获取摸鱼日历失败");
            return   ResponseResult.okResult(appAccountEnum);
        }

    }

    public ResponseResult GetHistoryTody(){
        AppAccountEnum appAccountEnum = new AppAccountEnum();
        String historytody = historyTody.historytody().replaceAll("&nbsp;&nbsp","").replaceAll(",","");

        String[] ends = historytody.split("end");
//        appAccountEnum.setMsg(historytody);
        return    ResponseResult.okResult( ends);
    }

    public ResponseResult GetBaidu(String msg){
        HashMap baiDu=null;
        AppAccountEnum appAccountEnum = new AppAccountEnum();
        try {
            baiDu = getBaiDu.getBaiDu(msg);
        }catch (Exception e){

        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append((String)baiDu.get("title")).append("\n").append((String) baiDu.get("content")).append("\n");
        appAccountEnum.setMsg(stringBuffer.toString());
        String img = (String) baiDu.get("img");
        if(!img.equals("无"))
        {
            appAccountEnum.setUrl((String) baiDu.get("img"));
        }
        return   ResponseResult.okResult( appAccountEnum);
    }
@Autowired
private GetChatGpt getChatGpt;

    public ResponseResult AccountInfo(String msg){

        String pattern = "https?://[^\\s]+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(msg);
        StringBuilder stringBuilder=null;
        if(m.find()) {
           stringBuilder = getPicture.get(msg);
        }

        Message message1 = new Message();
        message1.setKeymessage(msg);
        List<Message> messages = service.Get_Message_by_key(message1);
        int size = messages.size();
        String valuemessage = "";
        AppAccountEnum appAccountEnum = new AppAccountEnum();
        if (messages.size() != 0) {
            if (messages.size() == 1) {
                valuemessage = messages.get(0).getValuemessage();
                if (valuemessage.equals("")) {
                   appAccountEnum.setUrl(  messages.get(0).getUrl());
                } else {
                    String v = messages.get(0).getValuemessage();
                    String[] split = v.split("\\[");
                    if(split[0].equals("")){
                        appAccountEnum.setUrl(  messages.get(0).getUrl().replaceAll("\n",""));
                    }
                    appAccountEnum.setMsg(split[0]);

                }

            } else {
                double d = Math.random();
                int i = (int) (d * size);
                valuemessage = messages.get(i).getValuemessage();
                if (valuemessage.equals("")) {
                    try {
                        appAccountEnum.setMsg(  messages.get(0).getUrl());
                    }catch (Exception e){

                    }

                } else {
                    String v = messages.get(i).getValuemessage();
                    appAccountEnum.setMsg(messages.get(0).getValuemessage());
                }
            }
            return   ResponseResult.okResult( appAccountEnum);
        }else
        {
            try {
                AppAccountEnum appAccountEnum1 = new AppAccountEnum();
                String talk = getChatGpt.Get(msg);
                appAccountEnum1.setMsg(talk);
                return   ResponseResult.okResult( appAccountEnum1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return   ResponseResult.okResult( appAccountEnum);

    }



    public ResponseResult CartoonNews(){
        try {
            String news = cartoon_news.getNews();
            String[] ends = news.split("end");
            ArrayList<String> strings = new ArrayList<>();
            for (String end : ends) {
                if (!end.equals("")){
                    strings.add(end);
                }

            }
            return ResponseResult.okResult(strings);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.okResult("接口失效");
        }
    }

    public ResponseResult Eat_today(){
        if (today_eat == null) {
            this.today_eat = todayEatService.Send_Today_Eat_Message();
        }
        int size = today_eat.size();
        double v = nextDouble();
        double v1 = size * v;
        Today_Eat today_eat1 = today_eat.get((int) v1);
        String qq = today_eat1.getQq();
        String[] split1 = today_eat1.getMessage().split("\n");
        String message =split1[0];
        String pattern = "https?://[^\\s]+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(split1[1]);
        String[] split = message.split("\n");
        String messagetext= split[0];
        String image ="";
        Today_Eat_app today_eat = new Today_Eat_app();
        today_eat.setMessage( message);
        today_eat.setQq(qq);
        if(m.find()){
            String group = m.group();
            image = group.replaceAll("]", "");
            today_eat.setUrl(image);
            return ResponseResult.okResult(today_eat);
        }else{
            today_eat.setError("图片获取失效");
            return ResponseResult.okResult(today_eat);
        }




    }



}
