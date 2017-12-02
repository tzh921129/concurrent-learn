package com.example.block;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author tzh92
 */
public class MyBlockingQue {
    public static void main(String[] args) {
        final BlockingQueue<Object> blockingQ = new ArrayBlockingQueue<Object>(10);

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        try {
            singleThreadPool.execute((Runnable) blockingQ.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        singleThreadPool.shutdown();
    }
}
