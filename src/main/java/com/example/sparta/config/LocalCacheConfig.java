//package com.example.sparta.config;
//
//import com.github.benmanes.caffeine.cache.Caffeine;
//import java.time.Duration;
//import java.util.List;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.caffeine.CaffeineCache;
//import org.springframework.cache.support.SimpleCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class LocalCacheConfig {
//
//    @Bean
//    public CacheManager cacheManager() {
//        var orderDetail = new CaffeineCache("order:detail",
//                Caffeine.newBuilder()
//                        .maximumSize(10_000) // 캐시 용량
//                        .expireAfterWrite(Duration.ofMinutes(5)) // TTL 설정, 캐싱 5분 후 제거
//                        .build());
//
//        var orderSummaries = new CaffeineCache("order:summaries",
//                Caffeine.newBuilder()
//                        .maximumSize(20_000)
//                        .expireAfterWrite(Duration.ofSeconds(30))
//                        .build());
//
//        var productList = new CaffeineCache("product:list",
//                Caffeine.newBuilder()
//                        .maximumSize(20_000)
//                        .expireAfterWrite(Duration.ofSeconds(30))
//                        .build());
//
//        var mgr = new SimpleCacheManager();
//        mgr.setCaches(List.of(orderDetail, orderSummaries, productList));
//        return mgr;
//    }
//}
