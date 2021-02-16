package org.tiger.common.threadpool;

import java.util.concurrent.*;

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
                    poolExecutor = new ThreadPoolExecutor(50, 200, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>(500),
                            new ThreadFactory() {
                                public Thread newThread(Runnable r) {
                                    Thread s = Executors.defaultThreadFactory().newThread(r);
                                    //默认的线程为非守护线程，需要改成守护线程
                                    s.setDaemon(true);
                                    return s;
                                }
                            });
                }
            }
        }
        return poolExecutor;
    }
}
