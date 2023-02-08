package com.mybatisplus.utils.Qiniuyun.util;

import com.mybatisplus.utils.MakeNeko;
import com.mybatisplus.utils.Qiniuyun.ResponsePojo.Resopnse_Qiniuyun_JsonRootBean;
import com.qiniu.common.QiniuException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class Get_Url_in_Text_and_get_Picture_from_Url {
    @Autowired
    private QiNuYun_util qiNuYunUtil;
    public String getPicture(String text){
        String re ="";
        String regex = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\&%\\+\\$#_=]*)?";
        // 创建正则表达式对象
        Pattern r = Pattern.compile(regex);
        // 创建匹配器
        Matcher m = r.matcher(text);
        // 查找匹配的字符串
        int i=0;
        String s;
        while (m.find()) {
            try {
                i++;
                Resopnse_Qiniuyun_JsonRootBean resopnse_qiniuyun_jsonRootBean = qiNuYunUtil.ImageCensor("https://mini.s-shot.ru/1024x768/PNG/800/?" + m.group());
                s = resopnse_qiniuyun_jsonRootBean.returnMessage();
                if (s.equals("yes")){
                    re=re+ MakeNeko.MakePicture("https://mini.s-shot.ru/1024x768/PNG/800/?"+m.group());
                }
                else if (s.equals("no")){
                    re=re+"您发送的第"+i+"个链接解析后的图片经过审核可能为有违规内容 请谨慎访问"+ MakeNeko.MakePicture("https://mini.s-shot.ru/1024x768/PNG/800/?"+m.group());
                }
                else{
                    re=re+"图片可能有违规内容";
                }
            } catch (QiniuException e) {
                e.printStackTrace();
                re="七牛云连接超时 现图片直接发送"+ MakeNeko.MakePicture("https://mini.s-shot.ru/1024x768/PNG/800/?"+m.group());
            }

        }
        if (re.equals("")){
            return null;
        }
        return re;
    }
}
