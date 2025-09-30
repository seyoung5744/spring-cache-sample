package com.example.sparta.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

import com.github.benmanes.caffeine.cache.Caffeine; // 주의: caffeine 패키지

@Configuration
public class LocalCacheConfig {

    @Bean
    public CacheManager cacheManager() {
        var orderDetail = new CaffeineCache(
                "order:detail",
                Caffeine.newBuilder()
                        .maximumSize(10_000)
                        .expireAfterWrite(Duration.ofMinutes(5))
                        .build()
        );

        var orderSummaries = new CaffeineCache(
                "order:summaries",
                Caffeine.newBuilder()
                        .maximumSize(20_000)
                        .expireAfterWrite(Duration.ofSeconds(30))
                        .build()
        );

        var productList = new CaffeineCache(
                "product:list",
                Caffeine.newBuilder()
                        .maximumSize(20_000)
                        .expireAfterWrite(Duration.ofSeconds(30))
                        .build()
        );

        var manager = new SimpleCacheManager();
        manager.setCaches(List.of(orderDetail, orderSummaries, productList));
        return manager;
    }
}
