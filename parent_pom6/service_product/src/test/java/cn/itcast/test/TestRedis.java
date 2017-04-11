package cn.itcast.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class TestRedis {

	//注入 jedis 通过配置文件
	@Autowired
	private Jedis jedis;
	
	@Test
	public void testJedis1() throws Exception {
		Jedis jedis = new Jedis("192.168.200.128",6379);
		jedis.set("key1", "1");
	}
	
	@Test
	public void testJedis2() throws Exception {
		
		jedis.set("key2", "2");
	}
	
}
