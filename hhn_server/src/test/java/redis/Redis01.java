package redis;

import com.hhn.pojo.AccountUserDo;
import junit.framework.Assert;
import net.spy.memcached.MemcachedClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * Created by lenovo on 2014/12/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring.xml")
public class Redis01 {
    @Resource
    private MemcachedClient memcachedClient;
    @Test
    public void setValue() {
//        memcachedClient.set("panrui",7200, new AccountUserDo());
        System.out.println(memcachedClient.get("panrui"));
    }
/*@Resource
private JedisPool jedisPool;
    @Resource
    private JedisConnectionFactory jedisConnectionFactory;
    @Resource
    private JedisPoolConfig jedisPoolConfig;
    public Jedis getJedis(){
        return jedisConnectionFactory.getConnection().getNativeConnection();
    }
    public static T01 t01=new T01();
    public static String key = "hh";
//    @Cacheable(value = "commonData",key = "#{h}")
//    public String t2(String h) {
//        System.out.println("in.....method...");
//        return "World!";
//    }
//    @Before
    public void bef(){
        this.t01=new T01();
    }
@Test
    public void test01() {
//    JedisPool jedisPool=new JedisPool(jedisPoolConfig,env.toString())
//    Jedis jedis=jedisConnectionFactory.getConnection().getNativeConnection();
//    Jedis jedis=jedisPool.getResource();
            jedisPool.getResource().set("t01", "abcdefg");
        Assert.assertTrue(jedisConnectionFactory.getConnection().getNativeConnection().exists("t01"));
    }
    @Test
    public void test02() {
//        T01 t01=new T01();
        System.out.println("Hello " + t01.t2(key));
        System.out.println(t01.t2(key));
//        Assert.assertTrue(jedisPool.getResource().exists("commonData"));
    }
    @Test
    public void test03(){
        System.out.println(t01.t2(key));
    }

    //TestAop
    @Test
    public void testAop(){
//        new LoanPersonService().abc("aaa");
    }*/
}
