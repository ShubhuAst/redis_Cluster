package com.cluster.redis.config;

import com.cluster.redis.pojo.User;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${redis.jedis.pool.max-total}")
    private int maxTotal;
    @Value("${redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${redis.jedis.pool.min-idle}")
    private int minIdle;

    private String redis_ip = "127.0.0.1";

    @Bean
    public JedisClientConfiguration getJedisClientConfiguration() {
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder JedisPoolingClientConfigurationBuilder = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration
                .builder();
        GenericObjectPoolConfig GenericObjectPoolConfig = new GenericObjectPoolConfig();
        GenericObjectPoolConfig.setMaxTotal(maxTotal);
        GenericObjectPoolConfig.setMaxIdle(maxIdle);
        GenericObjectPoolConfig.setMinIdle(minIdle);
        return JedisPoolingClientConfigurationBuilder.poolConfig(GenericObjectPoolConfig).build();
    }

    @Bean
    public JedisConnectionFactory getClusterConnectionFactory() {
        System.out.println("Ip of Redis container:" + redis_ip);
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
        clusterConfig.addClusterNode(new RedisNode(redis_ip, 30001));
        clusterConfig.addClusterNode(new RedisNode(redis_ip, 30002));
        clusterConfig.addClusterNode(new RedisNode(redis_ip, 30003));
        clusterConfig.addClusterNode(new RedisNode(redis_ip, 30004));
        clusterConfig.addClusterNode(new RedisNode(redis_ip, 30005));
        clusterConfig.addClusterNode(new RedisNode(redis_ip, 30006));

        return new JedisConnectionFactory(clusterConfig, getJedisClientConfiguration());
    }

    @Bean(name = "cluster")
    public RedisTemplate<String, Object> redisTemplateForCluster() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(getClusterConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public HashOperations<String, String, User> getRedisTemplateForHash() {
        return redisTemplateForCluster().opsForHash();
    }

    @Bean
    public RedisConnectionFactory getStandaloneConnectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName("127.0.0.1");
        connectionFactory.setPort(6379);
        return connectionFactory;
    }

    @Bean(name = "standalone")
    public RedisTemplate<String, String> redisTemplateForStandalone() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(getStandaloneConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        return redisTemplateForStandalone();
    }
}
