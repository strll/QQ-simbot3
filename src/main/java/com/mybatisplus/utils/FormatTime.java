package com.mybatisplus.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.utils
 * @date 2022/12/7 19:28
 */
public class FormatTime {
    public String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
