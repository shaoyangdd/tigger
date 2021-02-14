package org.tiger.common.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 *
 * @author 康绍飞
 * @date 2020-01-22
 */
public class ThreadPool {

    private static ThreadPoolExecutor poolExecutor;

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        if (poolExecutor == null) {
            synchronized (ThreadPool.class) {
                if (poolExecutor == null) {
                    //TODO 临时先这样配置，后面调整参数
                    poolExecutor = new ThreadPoolExecutor(50, 200, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>(500));
                }
            }
        }
        return poolExecutor;
    }
}
