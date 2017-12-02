package net.jcip.examples;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.*;

/**
 * BoundedExecutor
 * <p/>
 * 使用Semaphore来遏制任务的提交
 * Using a Semaphore to throttle task submission
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class BoundedExecutor {
    private final Executor exec;
    private final Semaphore semaphore;

    public BoundedExecutor(Executor exec, int bound) {
        this.exec = exec;
        this.semaphore = new Semaphore(bound);
    }

    public void submitTask(final Runnable command)
            throws InterruptedException {
        semaphore.acquire();
        try {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        command.run();
                    } finally {
                        semaphore.release();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        BoundedExecutor boundedExec = new BoundedExecutor(exec, 1);
        Future<String> header, footer;
        Runnable task = new Runnable() {
            int sum = 0;

            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    sum += i;
                }
                System.out.println("sum=" + sum);
            }
        };
        boundedExec.submitTask(task);
        boundedExec.submitTask(task);
    }
}
