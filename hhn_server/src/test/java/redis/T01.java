package redis;

import org.springframework.cache.annotation.Cacheable;

/**
 * Created by lenovo on 2014/12/6.
 */
public class T01 {
    @Cacheable("commonData")
    public String t2(String h) {
        System.out.println("in.....method...");
        return "World!";
    }
}
