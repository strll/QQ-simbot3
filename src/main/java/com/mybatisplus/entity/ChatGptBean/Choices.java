/**
  * Copyright 2023 bejson.com 
  */
package com.mybatisplus.entity.ChatGptBean;

import com.mybatisplus.entity.ChatGptBean.Message ;

/**
 * Auto-generated: 2023-03-03 14:42:37
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Choices {

    private Message message;
    private String finish_reason;
    private int index;
    public void setMessage(Message message) {
         this.message = message;
     }
     public Message getMessage() {
         return message;
     }

    public void setFinish_reason(String finish_reason) {
         this.finish_reason = finish_reason;
     }
     public String getFinish_reason() {
         return finish_reason;
     }

    public void setIndex(int index) {
         this.index = index;
     }
     public int getIndex() {
         return index;
     }

}