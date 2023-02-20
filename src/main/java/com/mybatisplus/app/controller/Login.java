package com.mybatisplus.app.controller;

import com.mybatisplus.app.service.LoginService;
import com.mybatisplus.entity.app.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class Login {
@Autowired
private LoginService loginService;
    @GetMapping("/login")
    public ResponseResult load(String username,String password) {
    return loginService.login(username, password);
    }
}
