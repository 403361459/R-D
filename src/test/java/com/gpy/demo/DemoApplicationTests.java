package com.gpy.demo;
import com.gpy.demo.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;


@SpringBootTest
class DemoApplicationTests {


    @Test
    void contextLoads() {
        System.out.print("test");

    }
    @Autowired
    private RedisTemplate  redisTemplate;

    @Test
    public void set(){
        Users user=new Users();
        user.setId(8);
        user.setEmail("403361459@qq.com");
        user.setCode("88449848");
        user.setPassword("48498");
        user.setPhonenum("18452483878");
        user.setUsername("ggg");
        user.setRole("ggg");
        user.setStatus(0);
        redisTemplate.opsForValue().set("myKey2","myValue2");
        redisTemplate.opsForValue().set("user:1", user);
        redisTemplate.opsForValue().set("user:2", user);
        System.out.println(redisTemplate.opsForValue().get("myKey2"));
    }


}
