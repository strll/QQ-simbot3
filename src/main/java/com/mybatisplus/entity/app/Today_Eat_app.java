package com.mybatisplus.entity.app;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Today_Eat_app implements Serializable {
    private static final long serialVersionUID = 2L;


    private Integer  id;

    private String message;

    private String qq;

    private String url;
    private String error;
    @Override
    public String toString() {
        return "今天吃" + message +"吧";
    }
}
