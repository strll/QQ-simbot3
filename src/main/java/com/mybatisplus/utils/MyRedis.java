package com.mybatisplus.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class MyRedis {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public StringRedisTemplate getStringRedisTemplate(){
        return stringRedisTemplate;
    }
//增
    public String setAdd(String key,String value){
        Long add = stringRedisTemplate.opsForSet().add(key, value);
        return add+"";
    }
    //删
    public String setDelete(String key,String value){
        Long remove = stringRedisTemplate.opsForSet().remove(key, value);
        return remove+"";
    }
    //查
    public Boolean setfind(String key, String value){
        return stringRedisTemplate.opsForSet().isMember(key, value);
    }

    public Set<String> setfindAll(String key){
        return stringRedisTemplate.opsForSet().members(key);
    }


    // 修改 Set 中的某个值
    public Boolean setRevise(String key,String oldValue,String newValue){
        Long remove = stringRedisTemplate.opsForSet().remove(key, oldValue);
        Long add = stringRedisTemplate.opsForSet().add(key, newValue);
        if(add!=0){
            return false;
        }else{
            return true;
        }



    }


    //增
    public String listAddleft(String key,String value){
        Long aLong = stringRedisTemplate.opsForList().leftPush(key, value);
        return aLong+"";
    }
    public String listAddright(String key,String value){
        Long aLong = stringRedisTemplate.opsForList().rightPush(key, value);
        return aLong+"";
    }
    /*
 * 如果 count 为正数，则从左往右删除，直到删除 count 个元素为止。
如果 count 为负数，则从右往左删除，直到删除 count 个元素为止。
如果 count 为 0，则删除所有匹配的元素。
* */  //删
    public String listDelete(String key,String count,String value){
        Long remove = stringRedisTemplate.opsForList().remove(key, Long.parseLong(count), value);
        return remove+"";
    }
    public String listDelete_one(String key,String value){
        Long count=1l;
        Long remove = stringRedisTemplate.opsForList().remove(key,count, value);
        return remove+"";
    }

    public String listDelete_all(String key,String value){
        Long count=0l;
        Long remove = stringRedisTemplate.opsForList().remove(key,count, value);
        return remove+"";
    }

    //查
    public Boolean listisMember(String key, String value){
        Long index = stringRedisTemplate.opsForList().indexOf(key, value);
        if(index!=0l){
            return false;
        }else{
            return true;
        }
    }
    //改

    public List<String> listgetAll(String key){
        List<String> elements = stringRedisTemplate.opsForList().range(key, 0, -1);
return elements;
    }

public void listRevise(String key,String oldvalue,String newvalue){
    List<String> elements = stringRedisTemplate.opsForList().range(key, 0, -1);

    for(int i=0;i<elements.size();i++){
        if(elements.get(i).equals(key+":"+oldvalue)){
            stringRedisTemplate.opsForList().set(key, i, newvalue);
        }
    }

}



    /** -------------------key相关操作--------------------- */

    /**
     * 删除key
     *
     * @param key
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 批量删除key
     *
     * @param keys
     */
    public void delete(Collection<String> keys) {
        stringRedisTemplate.delete(keys);
    }

    /**
     * 序列化key
     *
     * @param key
     * @return
     */
    public byte[] dump(String key) {
        return stringRedisTemplate.dump(key);
    }

    /**
     * 是否存在key
     *
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return stringRedisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param date
     * @return
     */
    public Boolean expireAt(String key, Date date) {
        return stringRedisTemplate.expireAt(key, date);
    }

    /**
     * 查找匹配的key
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        return stringRedisTemplate.keys(pattern);
    }

    /** -------------------string相关操作--------------------- */

    /**
     * 设置指定 key 的值
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取指定 key 的值
     * @param key
     * @return
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 返回 key 中字符串值的子字符
     * @param key
     * @param start
     * @param end
     * @return
     */
    public String getRange(String key, long start, long end) {
        return stringRedisTemplate.opsForValue().get(key, start, end);
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
     *
     * @param key
     * @param value
     * @return
     */
    public String getAndSet(String key, String value) {
        return stringRedisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)
     *
     * @param key
     * @param offset
     * @return
     */
    public Boolean getBit(String key, long offset) {
        return stringRedisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 批量获取
     *
     * @param keys
     * @return
     */
    public List<String> multiGet(Collection<String> keys) {
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }


}
