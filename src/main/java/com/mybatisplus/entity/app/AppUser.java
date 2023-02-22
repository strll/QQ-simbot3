package com.mybatisplus.entity.app;


import com.baomidou.mybatisplus.annotation.*;
import org.springframework.data.annotation.CreatedDate;
@TableName(value = "app_user")
public class AppUser {

  @TableId(value = "id", type = IdType.AUTO)
  private long id;
  private String username;
  private String password;
  private String mail;

  @TableField(fill = FieldFill.INSERT)
  private String createDate;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getName() {
    return username;
  }

  public void setName(String name) {
    this.username = name;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }


  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

}
