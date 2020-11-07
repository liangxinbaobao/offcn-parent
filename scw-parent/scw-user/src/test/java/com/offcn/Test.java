package com.offcn;

import com.offcn.user.ScwUserStart;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: lhq
 * @Date: 2020/10/22 10:56
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ScwUserStart.class})
public class Test {

   // private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;    //数据类型单一  少了存入数据没有序列化操作  与redisTemplate不互通


    @org.junit.Test
    public void saveValue(){
        stringRedisTemplate.opsForValue().set("message","欢迎来到优就业学习");
    }

}
