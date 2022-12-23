package moe.salamanda.salamanda.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;


    private final String DEFAULT_KEY_PREFIX = "SALAMANDA_";
    public final int EXPIRE_TIME = 1;
    public final TimeUnit EXPIRE_TIME_TYPE = TimeUnit.DAYS;

    //LoginServiceCommon
    public final String DEFAULT_USERNAME_PREFIX = "USERNAME_";
    public final String DEFAULT_ATTRIBUTE_PREFIX = "ATTRIBUTE_";

    public <K,V> void add(K key,V value){
        try{
            if(value != null){
                redisTemplate.opsForValue()
                        .set(DEFAULT_KEY_PREFIX + key.toString(),value.toString());
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Redis Cashing Failed");
        }
    }

    public <K,V> void add(K key,V value,long timeout,TimeUnit unit){
        try{
            if(value != null){
                redisTemplate.opsForValue()
                        .set(DEFAULT_KEY_PREFIX + key.toString(),value.toString(),timeout,unit);
            }
        }
        catch (Exception e){
            throw new RuntimeException("Redis Cashing Failed");
        }
    }

    public <K> String get(K key){
        String value;
        try{
            value = redisTemplate.opsForValue().get(DEFAULT_KEY_PREFIX + key.toString());
        }
        catch (Exception e){
            throw new RuntimeException("Redis Cache Getting Failed");
        }
        return value;
    }

    public String getCrypt(){
        String crypt = RandomService.getCyrptedCookie();
        boolean check = Boolean.getBoolean(redisTemplate.opsForValue().get(crypt));
        while (check == true) {
            crypt = RandomService.getCyrptedCookie();
            check = Boolean.getBoolean(redisTemplate.opsForValue().get(crypt));
        }
        return crypt;
    }

    public <K> void delete(K key){
        redisTemplate.delete(key.toString());
    }

    public <K> Boolean setExpire(K key,long timeout,TimeUnit unit){
        return redisTemplate.expire(key.toString(),timeout,unit);
    }

}
