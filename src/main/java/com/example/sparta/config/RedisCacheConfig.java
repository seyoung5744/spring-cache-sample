package com.example.sparta.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        var valueSerializer = new GenericJackson2JsonRedisSerializer();
        var redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues() // null 값 캐싱 비활성화
                .serializeKeysWith(
                        SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        SerializationPair.fromSerializer(valueSerializer)
                )
                .prefixCacheNameWith("v1:");

        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();

        configurationMap.put("order:detail",
                redisCacheConfiguration
                        .entryTtl(Duration.ofMinutes(5))
        );
        configurationMap.put("order:summaries",
                redisCacheConfiguration
                        .entryTtl(Duration.ofSeconds(30))
        );
        configurationMap.put("product:list",
                redisCacheConfiguration
                        .entryTtl(Duration.ofSeconds(5))
        );

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(configurationMap)
                .enableStatistics()
                .build();
    }

}
