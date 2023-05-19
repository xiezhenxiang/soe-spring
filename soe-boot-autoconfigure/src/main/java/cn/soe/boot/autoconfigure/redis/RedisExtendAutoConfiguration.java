package cn.soe.boot.autoconfigure.redis;

import cn.soe.util.concurrent.DistributedLocker;
import cn.soe.util.concurrent.lock.RedisDistributedLocker;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author xiezhenxiang 2023/5/9
 */
@AutoConfiguration
@ConditionalOnClass(StringRedisTemplate.class)
public class RedisExtendAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DistributedLocker.class)
    public DistributedLocker distributedLocker(StringRedisTemplate redisTemplate) {
        return new RedisDistributedLocker(redisTemplate);
    }
}
