package cn.soe.util.common;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xiezhenxiang 2020/7/27
 */
@Slf4j
public class ThreadUtils {

    private final ThreadPoolExecutor executor;
    private final int queueSize;
    private final int poolSize;

    public static ThreadUtils defaultInstance() {
        int persistThreadNum = Runtime.getRuntime().availableProcessors() / 2 + 2;
        return new ThreadUtils(persistThreadNum, 20);
    }

    public ThreadUtils(int poolSize, int queueSize) {
        this.poolSize = poolSize;
        this.queueSize = queueSize;
        ThreadPoolExecutor.CallerRunsPolicy callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
        this.executor = new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize), callerRunsPolicy);
    }

    public synchronized void execute(Runnable runnable) {

        while (blocking()) {
        }
        executor.execute(runnable);
    }

    /**
     * 线程池是否阻塞
     * @author xiezhenxiang 2020/7/27
     **/
    public boolean blocking() {
        return executor.getPoolSize() == poolSize && executor.getQueue().size() == queueSize;
    }

    public boolean empty() {
        return executor.getActiveCount() + executor.getQueue().size() == 0;
    }

    public void closeWithSafe() {
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }
}
