package com.mybatisplus.app.controller;


import com.mybatisplus.app.service.LoginService;
import com.mybatisplus.app.service.SendVerification_codeService;
import com.mybatisplus.utils.app.SendqqEmailService;
import com.mybatisplus.entity.app.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private SendVerification_codeService sendqEmailService;
    @GetMapping("/login")
    public ResponseResult load(String username, String password) {
        return loginService.login(username, password);
    }
    @GetMapping("/verification_code")//注册获取验证码
    public ResponseResult verification_code(String email) {
        return sendqEmailService.send(email);
    }

    @GetMapping("/register")//注册
    public ResponseResult register(String username, String password,String email,String code) {
        return loginService.register(username, password,email,code);
    }

    @GetMapping("/resetpassword_code")//修改密码获取验证码
    public ResponseResult resetpassword_code(String email) {
        return sendqEmailService.resetpassword(email);
    }
    @GetMapping("/resetpassword")//修改密码
    public ResponseResult resetpassword(String username, String password,String email,String code) {
        return loginService.resetpassword(username, password,email,code);
    }



}
