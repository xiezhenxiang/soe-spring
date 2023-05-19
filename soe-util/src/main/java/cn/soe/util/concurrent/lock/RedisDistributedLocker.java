package cn.soe.util.concurrent.lock;

import cn.soe.util.concurrent.DistributedLocker;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author xiezhenxiang 2023/5/11
 */
public class RedisDistributedLocker implements DistributedLocker {

    private final StringRedisTemplate redisTemplate;

    private final DefaultRedisScript<Long> redisScript;

    public RedisDistributedLocker(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisScript = new DefaultRedisScript<>();
        this.redisScript.setResultType(Long.class);
        this.redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("unlock.lua", RedisDistributedLocker.class)));
    }

    @Override
    public void lock(String lockKey,int waitTime, int holdTime) {
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Boolean success = setIfAbsent(lockKey, holdTime, timeUnit);
        long nanoWaitForLock = timeUnit.toNanos(DEFAULT_WAIT_TIME);
        long start = System.nanoTime();
        while ((System.nanoTime() - start < nanoWaitForLock) && (success == null || !success)) {
            success = setIfAbsent(lockKey, waitTime, timeUnit);
            if(success != null && success){
                break;
            }
        }
        if(success == null || !success){
            throw new RuntimeException("get distributed lock by key["+lockKey+"] timeout");
        }
    }

    @Override
    public void unlock(String lockKey) {
        redisTemplate.execute(redisScript, Collections.singletonList(lockKey), lockKey);
    }

    private Boolean setIfAbsent(String lockKey, int leaseTime, TimeUnit unit){
        return redisTemplate.opsForValue().setIfAbsent(lockKey, "lock", leaseTime, unit);
    }
}
