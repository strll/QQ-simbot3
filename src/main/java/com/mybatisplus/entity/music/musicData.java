/**
  * Copyright 2023 bejson.com 
  */
package com.mybatisplus.entity.music;
import java.util.List;

/**
 * Auto-generated: 2023-02-10 11:26:6
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class musicData {

    private long Id;
    private String Music;
    private String Cover;
    private List<String> Singer_Array;
    private String Singer;
    private String Url;
    private String Music_Url;
    public void setId(long Id) {
         this.Id = Id;
     }
     public long getId() {
         return Id;
     }

    public void setMusic(String Music) {
         this.Music = Music;
     }
     public String getMusic() {
         return Music;
     }

    public void setCover(String Cover) {
         this.Cover = Cover;
     }
     public String getCover() {
         return Cover;
     }

    public void setSinger_Array(List<String> Singer_Array) {
         this.Singer_Array = Singer_Array;
     }
     public List<String> getSinger_Array() {
         return Singer_Array;
     }

    public void setSinger(String Singer) {
         this.Singer = Singer;
     }
     public String getSinger() {
         return Singer;
     }

    public void setUrl(String Url) {
         this.Url = Url;
     }
     public String getUrl() {
         return Url;
     }

    public void setMusic_Url(String Music_Url) {
         this.Music_Url = Music_Url;
     }
     public String getMusic_Url() {
         return Music_Url;
     }

}