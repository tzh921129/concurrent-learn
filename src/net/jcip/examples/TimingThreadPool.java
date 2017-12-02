package net.jcip.examples;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * TimingThreadPool
 * <p/>
 * Thread pool extended with logging and timing
 *
 * @author Brian Goetz and Tim Peierls
 */
public class TimingThreadPool extends ThreadPoolExecutor {

    public TimingThreadPool() {
        super(3, 3, 0L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    private final Logger log = Logger.getLogger("TimingThreadPool");
    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTime = new AtomicLong();
    private static final int LOOP_VALUE = 10000;


    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        log.info(String.format("Thread %s: start %s", t, r));
        startTime.set(System.nanoTime());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try {
            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();
            numTasks.incrementAndGet();
            totalTime.addAndGet(taskTime);
            log.info(String.format("Thread %s: end %s, time=%dns",
                    Thread.currentThread().getName(), r, taskTime));
        } finally {
            super.afterExecute(r, t);
            startTime.remove();
        }
    }

    @Override
    protected void terminated() {
        try {
            log.info(String.format("Terminated: avg time=%dns",
                    totalTime.get() / numTasks.get()));
        } finally {
            super.terminated();
        }
    }

    public static void main(String[] args) {
        Runnable task = new Runnable() {
            private int sum = 0;

            @Override
            public void run() {
                for (int i = 0; i < LOOP_VALUE; i++) {
                    sum += i;
                }
                System.out.println(sum);
            }
        };
        TimingThreadPool pool = new TimingThreadPool();
        pool.execute(task);
        pool.execute(task);
        pool.execute(task);
        pool.shutdown();
    }
}
