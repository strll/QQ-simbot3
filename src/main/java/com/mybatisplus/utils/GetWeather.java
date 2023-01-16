package com.mybatisplus.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class GetWeather {
    @Value("${key}")
    String key;

    public HashMap<Integer,HashMap<String, String>> getBetterWeather(String city) throws IOException {
        Connection.Response execute = Jsoup.connect("https://geoapi.qweather.com/v2/city/lookup?&key="+key+"&location="+city)
                .postDataCharset("UTF-8")
                .ignoreContentType(true)
                .execute();
        String body = execute.body();

        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONArray location = jsonObject.getJSONArray("location");
        JSONObject o = (JSONObject)location.get(0);
        String lat = o.getString("lat");
        String lon = o.getString("lon");
        System.out.println(lat+"="+lon);
        Connection.Response weather = Jsoup.connect("https://devapi.qweather.com/v7/weather/3d?key=e0757a5474934a88ae69a2e8f2746a83&location="+lon+","+lat+"&lang=zh")
                .postDataCharset("UTF-8")
                .ignoreContentType(true)
                .execute();
        String stringweater = weather.body();
  
        JSONObject weatherJson = JSONObject.parseObject(stringweater);
        HashMap<Integer,HashMap<String, String>> remap = new HashMap<Integer,HashMap<String, String>> ();
        JSONArray daily = weatherJson.getJSONArray("daily");
        for (int i = 0; i < 3; i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            JSONObject o1 =(JSONObject) daily.get(i);
            hashMap.put("预报日期",o1.getString("fxDate"));
            hashMap.put("最高温度",o1.getString("tempMax"));
            hashMap.put("最低温度",o1.getString("tempMin"));
            hashMap.put("白天天气状况",o1.getString("textDay"));
            hashMap.put("夜晚天气状况",o1.getString("textNight"));
            hashMap.put("白天风向",o1.getString("windDirDay"));
            hashMap.put("白天风力等级",o1.getString("windScaleDay"));
            hashMap.put("夜间当天风向",o1.getString("windDirNight"));
            hashMap.put("夜间风力等级",o1.getString("windScaleNight"));
            hashMap.put("能见度",o1.getString("vis"));
            hashMap.put("相对湿度",o1.getString("humidity"));
            hashMap.put("当天总降水量",o1.getString("precip"));
            remap.put(i,hashMap);
        }

        return remap;
    }


    public ArrayList<HashMap<String, String>> getWeather(String where) {
        ArrayList<HashMap<String, String>> array = new ArrayList<>();
        String body = "";
        try {
            Connection.Response response = Jsoup.connect("https://api.seniverse.com/v3/weather/daily.json?key=SCYrvkytJze9qyzOh&location=" + where + "&language=zh-Hans&unit=c")
                    //.cookies(cookies)
                    .postDataCharset("UTF-8")
                    .ignoreContentType(true)
                    .execute();
            body = response.body(); // 获取html原始文本内容
            System.out.println(body);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.parseObject(body);


        JSONArray results = jsonObject.getJSONArray("results");

        JSONArray results1 = (JSONArray) jsonObject.getJSONArray("results");

        ArrayList<HashMap<String, String>> add3=null;

        for (Object o : results1) {
            JSONObject o1 = (JSONObject) o;
            JSONArray daily2 = o1.getJSONArray("daily");
            JSONObject location = (JSONObject) o1.get("location");
            String name = location.get("name").toString();
            JSONArray daily = jsonObject.getJSONArray("results");
            JSONObject o2 = (JSONObject) daily.get(0);
            JSONArray daily1 = o2.getJSONArray("daily");
            HashMap<String, String> hashMap = new HashMap<>();
            HashMap<String, String> hashMap1 = new HashMap<>();
            HashMap<String, String> hashMap2 = new HashMap<>();

                Object o21 = daily2.get(0);
                JSONObject object = (JSONObject) o21;

            Object o22 = daily2.get(1);
            JSONObject object1 = (JSONObject) o22;

            Object o23 = daily2.get(2);
            JSONObject object2 = (JSONObject) o23;

            ArrayList<HashMap<String, String>> add1 = add(hashMap, array, o21);
            ArrayList<HashMap<String, String>> add2 = add(hashMap1, array, o22);
             add3 = add(hashMap2, array, o23);
            }
        return  add3;
        }
        private static ArrayList<HashMap<String, String>> add( HashMap<String, String> hashMap,ArrayList<HashMap<String, String>> array,Object o21){
            JSONObject object = (JSONObject) o21;
            hashMap.put("当天最低温度", (String) object.get("low"));
            hashMap.put("当天最高温度", (String) object.get("high"));
            hashMap.put("相对湿度，0~100，单位为百分比", (String) object.get("humidity"));
            hashMap.put("降水概率，范围0~100", (String) object.get("precip"));
            hashMap.put("降水量，单位mm", (String) object.get("wind_scale"));
            hashMap.put("日期", (String) object.get("date"));
            hashMap.put("风力等级", (String) object.get("rainfall"));
            hashMap.put("风速，单位为km/h公里每小时或mph英里每小时", (String) object.get("wind_speed"));
            hashMap.put("风向角度，范围0~360，0为正北，90为正东，180为正南，270为正西", (String) object.get("wind_direction_degree"));
            hashMap.put("风向文字", (String) object.get("wind_direction"));
       //     hashMap.put("晚间天气现象代码", (String) object.get("code_night"));
            hashMap.put("晚间天气", (String) object.get("text_night"));
         //   hashMap.put("白天天气现象代码", (String) object.get("code_day"));
            hashMap.put("白天天气", (String) object.get("text_day"));
            array.add(hashMap);
            return array;

        }

}
