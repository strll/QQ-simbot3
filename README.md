yml文件配置如下

# QQ-simbot3
spring:

  servlet:
    multipart:
      enabled: true
      maxFileSize: 500MB
      maxRequestSize: 500MB
      file-size-threshold: 500MB
  # 配置数据源信息
  datasource:
    # 配置数据源类型

    # 配置连接数据库信息
    driver-class-name: com.mysql.cj.jdbc.Driver
    url:
    username: 
    password: 
#    driver-class-name: com.mysql.cj.jdbc.Driver
#    url:
#    username:
#    password: 
#
  redis:
    host: 
    port: 6379  
    password: 
    database: 
minio:
  accessKey: 
  secretKey:
  bucket: 
  endpoint:
  readPath: 
logging:
  level:
    love.forte: debug
simbot:
  core:
    bot-resource-type: both
  component:
    mirai:
      protocol: ANDROID_PAD

server:
  #端口号
  port: 

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: com/mybatisplus/mapper/**/*.xml

key:
music_cookie:
userAgent:
tianxingkey:
music_key:

openai_api:
soutu_api:
qi:
  AccessKey: 
  SecretKey:

picture_key: 


qqemail:
  qqemail: 
  password: 

ChatGpt:
  url: 
  key: 
