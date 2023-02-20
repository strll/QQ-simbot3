package com.mybatisplus.app.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatisplus.entity.app.AppUser;
import com.mybatisplus.entity.app.ResponseResult;
import com.mybatisplus.mapper.AppLoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private AppLoginMapper appLoginMapper;

    public ResponseResult login(String username, String password){
        String s = DigestUtil.md5(password).toString();
        QueryWrapper<AppUser> appUserQueryWrapper = new QueryWrapper<>();
        appUserQueryWrapper.eq("username",username);
        appUserQueryWrapper.eq("password",password);
        boolean exists = appLoginMapper.exists(appUserQueryWrapper);
        return ResponseResult.okResult(exists);
    }
}
