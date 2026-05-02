package com.canda.weatherapi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${weather.cache.geo.limit}")
    private long geoCacheLimit;

    @Value("${weather.cache.weather.limit}")
    private long weatherCacheLimit;

    @Value("${weather.cache.expire.duration}")
    private Duration cacheExpiration;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        cacheManager.registerCustomCache("weatherCache", 
                Caffeine.newBuilder()
                        .maximumSize(weatherCacheLimit)
                        .expireAfterWrite(cacheExpiration)
                        .build());
                        
        cacheManager.registerCustomCache("geoCache", 
                Caffeine.newBuilder()
                        .maximumSize(geoCacheLimit)
                        .expireAfterWrite(cacheExpiration)
                        .build());
                        
        return cacheManager;
    }
}
