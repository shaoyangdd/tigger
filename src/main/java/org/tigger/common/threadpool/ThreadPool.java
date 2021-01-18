package org.tigger.common.threadpool;

import java.util.concurrent.*;

public class ThreadPool {

    private static ThreadPoolExecutor poolExecutor;

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        if (poolExecutor != null) {
            synchronized (ThreadPool.class) {
                if (poolExecutor != null) {
                    //TODO 临时先这样配置，后面调整参数
                    poolExecutor  = new ThreadPoolExecutor(5, 20, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>(500));
                }
            }
        }
        return poolExecutor;
    }
}
