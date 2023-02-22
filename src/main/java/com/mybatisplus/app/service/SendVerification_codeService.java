package com.mybatisplus.app.service;

import com.mybatisplus.entity.app.ResponseResult;
import com.mybatisplus.utils.MyRedis;
import com.mybatisplus.utils.app.SendqqEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class SendVerification_codeService {
    @Autowired
    private SendqqEmailService sendqEmailService;
    @Autowired
    private MyRedis myRedis;
    public ResponseResult send(String email){
        try {
            int value;//定义两变量
            Random ne=new Random();//实例化一个random的对象ne
            value=ne.nextInt(9999-1000+1)+1000;//为变量赋随机值1000-9999
            StringRedisTemplate stringRedisTemplate = myRedis.getStringRedisTemplate();
            stringRedisTemplate.opsForValue().set("Verification_codeService"+email, value+"", 60 * 5, TimeUnit.SECONDS);
            sendqEmailService.send(email,"你好这是你的验证码: "+value+" (验证码五分钟内有效)");
            return  ResponseResult.okResult("发送成功");

            }catch(Exception e){
            return  ResponseResult.okResult("发送失败");
        }
    }

    public ResponseResult resetpassword(String email){
        try {
            int value;//定义两变量
            Random ne=new Random();//实例化一个random的对象ne
            value=ne.nextInt(9999-1000+1)+1000;//为变量赋随机值1000-9999
            StringRedisTemplate stringRedisTemplate = myRedis.getStringRedisTemplate();
            stringRedisTemplate.opsForValue().set("resetpassword_codeService"+email, value+"", 60 * 5, TimeUnit.SECONDS);
            sendqEmailService.send(email,"你好这是你修改密码时的的验证码: "+value+" (验证码五分钟内有效)");
            return  ResponseResult.okResult("发送成功");
        }catch(Exception e){
            return  ResponseResult.okResult("发送失败");
        }
    }

}
