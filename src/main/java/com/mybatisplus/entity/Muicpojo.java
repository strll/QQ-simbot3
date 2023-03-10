package com.mybatisplus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Muicpojo {
  private String  Music;
    private String NickUser;
    private String    MusicUrl;
    private String    Picture;
    private String   Url;
}
