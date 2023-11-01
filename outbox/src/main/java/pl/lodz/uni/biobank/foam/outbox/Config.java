package pl.lodz.uni.biobank.foam.outbox;


import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;

@EnableCaching
@Configuration
@EnableWebSecurity
public class Config {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("cega-credentials");
    }

    //Clear cache every hour
    @CacheEvict(value = "cega-credentials", allEntries = true)
    @Scheduled(cron = "0 0 0 * * *")
    public void clearUsersCache() {
//    FIXME
    }
}
