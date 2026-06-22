package kr.co.mindpro.ipms.common.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;


@Configuration
    @EnableCaching
    public class CacheConfig {
        @Bean
        public CacheManager tempTokenCacheManager() {
            CaffeineCacheManager cacheManager = new CaffeineCacheManager("tempFileTokens");
            cacheManager.setCaffeine(Caffeine.newBuilder()
                    .expireAfterWrite(30, TimeUnit.MINUTES)
                    .maximumSize(1000));
            return cacheManager;
        }
    }

