package com.mybatisplus.app.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatisplus.entity.app.AppUser;
import com.mybatisplus.entity.app.ResponseResult;
import com.mybatisplus.mapper.AppLoginMapper;
import com.mybatisplus.utils.MyRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class LoginService {
    @Autowired
    private AppLoginMapper appLoginMapper;
    @Autowired
    private MyRedis myRedis;
    public ResponseResult login(String username, String password){
        String pawd = SecureUtil.md5(password);

        QueryWrapper<AppUser> appUserQueryWrapper = new QueryWrapper<>();
        appUserQueryWrapper.eq("username",username);
        appUserQueryWrapper.eq("password",pawd);
        boolean exists = appLoginMapper.exists(appUserQueryWrapper);
        return ResponseResult.okResult(exists);
    }
    //register

    public ResponseResult register(String username, String password,String email,String code){
try{
    String s = myRedis.get("Verification_codeService" + email);
    if (code.equals(s)){
        String pawd = SecureUtil.md5(password);
        AppUser appUser = new AppUser();
        appUser.setName(username);
        appUser.setPassword(pawd);
        appUser.setMail(email);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        appUser.setCreateDate(sdf.format(date));
        int insert = appLoginMapper.insert(appUser);
        if(insert==1){
            return ResponseResult.okResult("成功创建账号");
        }else{
            return ResponseResult.okResult("创建账号失败");}
    }else{
        return ResponseResult.okResult("验证码错误"); }
}catch(Exception e){
    return ResponseResult.okResult("验证码错误 可能已过期");
}




      
    }

    public ResponseResult resetpassword(String username, String password, String email, String code) {
        try{
            String s = myRedis.get("resetpassword_codeService" + email);
            if (code.equals(s)){
                String pawd = SecureUtil.md5(password);
                AppUser appUser = new AppUser();
                appUser.setName(username);
                appUser.setPassword(pawd);
                appUser.setMail(email);
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
                sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
                appUser.setCreateDate(sdf.format(date));

                QueryWrapper<AppUser> appUserQueryWrapper = new QueryWrapper<>();
                appUserQueryWrapper.eq("username",username);
                AppUser appUser1 = appLoginMapper.selectOne(appUserQueryWrapper);
                long id = appUser1.getId();
                appUser.setId(id);

                int insert = appLoginMapper.updateById(appUser);
                if(insert==1){
                    return ResponseResult.okResult("成功修改密码");
                }else{
                    return ResponseResult.okResult("创建修改密码");}
            }else{
                return ResponseResult.okResult("验证码错误"); }
        }catch(Exception e){
            return ResponseResult.okResult("验证码错误 可能已过期");
        }

    }
}
