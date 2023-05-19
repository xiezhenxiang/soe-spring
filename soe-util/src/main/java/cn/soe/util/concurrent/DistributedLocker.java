package cn.soe.util.concurrent;

/**
 * 分布式锁
 * @author xiezhenxiang 2023/5/11
 */
public interface DistributedLocker { ;

    /**
     * 获取锁等待时间 默认10min
     */
    int DEFAULT_WAIT_TIME = 1000 * 60 * 10;
    /**
     * 拿到锁最大持有时间 默认24h
     */
    int DEFAULT_HOLD_TIME = 1000 * 60 * 60 * 24;

    /**
     * 上锁
     **/
    void lock(String lockKey, int waitTime, int holdTime);

    /**
     * 释放锁
     */
    void unlock(String lockKey);

    default void lock(String lockKey) {
        lock(lockKey, DEFAULT_WAIT_TIME, DEFAULT_HOLD_TIME);
    }

    default void lock(String lockKey, int waitTime) {
        lock(lockKey, waitTime, DEFAULT_HOLD_TIME);
    }

    default <T> void exec(String lockKey, int waitTime, int holdTime, Runnable runnable) {
        try {
            lock(lockKey, waitTime, holdTime);
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            unlock(lockKey);
        }
    }

    default <T> void exec(String lockKey, Runnable runnable) {
        exec(lockKey, DEFAULT_WAIT_TIME, DEFAULT_HOLD_TIME, runnable);
    }

    default <T> void exec(String lockKey, int waitTime, Runnable runnable) {
        exec(lockKey, waitTime, DEFAULT_HOLD_TIME, runnable);
    }

}

