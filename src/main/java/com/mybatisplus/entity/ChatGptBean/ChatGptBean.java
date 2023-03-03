/**
  * Copyright 2023 bejson.com 
  */
package com.mybatisplus.entity.ChatGptBean;
import java.util.List;

/**
 * Auto-generated: 2023-03-03 14:42:37
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ChatGptBean {

    private String id;
    private String object;
    private long created;
    private String model;
    private Usage usage;
    private List<Choices> choices;
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setObject(String object) {
         this.object = object;
     }
     public String getObject() {
         return object;
     }

    public void setCreated(long created) {
         this.created = created;
     }
     public long getCreated() {
         return created;
     }

    public void setModel(String model) {
         this.model = model;
     }
     public String getModel() {
         return model;
     }

    public void setUsage(Usage usage) {
         this.usage = usage;
     }
     public Usage getUsage() {
         return usage;
     }

    public void setChoices(List<Choices> choices) {
         this.choices = choices;
     }
     public List<Choices> getChoices() {
         return choices;
     }

}