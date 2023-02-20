package com.mybatisplus.app.controller;

import com.mybatisplus.app.service.BotService;
import com.mybatisplus.app.service.LoginService;
import com.mybatisplus.entity.app.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@CrossOrigin
@RestController
@RequestMapping("/bot")

public class botContorller {
    @Autowired
    private BotService botService;
    @GetMapping("/CartoonNews")
    public ResponseResult CartoonNews() {
        return botService.CartoonNews();
    }
    @GetMapping("/Eattoday")
    public ResponseResult Eattoday() {
        return botService.Eat_today();
    }
    @GetMapping("/GetBaidu")
    public ResponseResult GetBaidu(String msg) {
        return botService.GetBaidu(msg);
    }

    @GetMapping("/GetHistoryTody")
    public ResponseResult GetHistoryTody() {
        return botService.GetHistoryTody();
    }

    @GetMapping("/GetMoYu")
    public ResponseResult GetMoYu() {
        return botService.GetMoYu();
    }
    @GetMapping("/GetNews")
    public ResponseResult GetNews() {
        return botService.GetNews();
    }

    @GetMapping("/GetNowWeather")
    public ResponseResult GetNowWeather(String city) {
        return botService.GetNowWeather(city);
    }

    @GetMapping("/GetTranslation")
    public ResponseResult GetTranslation(String msg) {
        return botService.GetTranslation(msg);
    }
    @GetMapping("/GetSong")
    public ResponseResult GetSong(String song) {
        return botService.GetSong(song);
    }

    @GetMapping("/GetWbTop10")
    public ResponseResult GetWbTop10() {
        return botService.GetWbTop10();
    }

    @GetMapping("/GetAccountInfo")
    public ResponseResult GetAccountInfo(String msg) {
            return botService.AccountInfo(msg);

    }


}
