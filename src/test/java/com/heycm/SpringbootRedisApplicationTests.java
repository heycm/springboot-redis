package com.heycm;

import com.heycm.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class SpringbootRedisApplicationTests {

    @Autowired
    RedisUtil redisUtil;

    @Test
    void contextLoads() throws InterruptedException {
        redisUtil.set("hhh", "123", 5, TimeUnit.SECONDS);
        System.out.println(redisUtil.get("hhh"));
        Thread.sleep(5000);
        System.out.println(redisUtil.get("hhh"));
    }

}
