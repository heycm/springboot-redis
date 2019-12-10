package com.heycm.demo;

import com.heycm.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TestDemo {

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void m1() throws InterruptedException {
//        redisUtil.set("key_1", "val_1");
//        System.out.println(redisUtil.get("key_1"));
//        redisUtil.expire("key_1", 5, TimeUnit.SECONDS);
//        Thread.sleep(5000);
//        System.out.println(redisUtil.get("key_1"));
    }
}
