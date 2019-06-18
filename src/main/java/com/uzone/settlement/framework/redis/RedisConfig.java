package com.uzone.settlement.framework.redis;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisConfig {

	/**
	 * 使用框架默认的template
	 */
	/**
	 * 使用方法 - 第一种（很早的方法了）
	 * 通过一个连接池的配置创建了RedisConnectionFactory
	 
	private RedisConnectionFactory connectionFactory = null;
	
	@Bean(name = "RedisConnectionFactory")
	public RedisConnectionFactory initRedisConnectionFactory() {
		if(this.connectionFactory != null) {
			return this.connectionFactory;
		}
		// Jedis连接池配置，用于优化直连浪费资源
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(50); 		// 设置最大连接数
		poolConfig.setMaxIdle(30);			// 设置最大空闲连接数
		poolConfig.setMaxWaitMillis(2000); 	// 最大等待时间，单位毫秒
		// 创建Jedis连接工厂
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
		connectionFactory.setHostName("192.168.11.131");
		connectionFactory.setPort(6379);
		connectionFactory.setPassword("jj123456");
		this.connectionFactory = connectionFactory;
		return connectionFactory;
	}
	*/
	/**
	 * 使用方法 - 第二种（使用最多的方法）
	 * redisTemplate,应该是类似于mongodbTemplate一样的，看起来就像好用的代名词
	 
	@Bean(name = "redisTemplate")
	public RedisTemplate<Object, Object> initRedisTemplate(){
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		//redisTemplate.setConnectionFactory(initConnectionFactory());
		return redisTemplate;
	}
	*/
	
	/************************** 使用方法 - 第三种 **************************/
	/* 缓存注解 */
	/************************** 使用方法 - 第三种 **************************/
}