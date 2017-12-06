package net.jcip.examples;

import java.util.concurrent.*;

/**
 * WorkerThread
 * <p/>
 * Serialized access to a task queue
 *
 * @author Brian Goetz and Tim Peierls
 */

public class WorkerThread extends Thread {
    //BlockingQueue取出的操作被阻塞的可能性小于使用同步的LinkedList的阻塞可能
    private final BlockingQueue<Runnable> queue;

    public WorkerThread(BlockingQueue<Runnable> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Runnable task = queue.take();
                task.run();
            } catch (InterruptedException e) {
                break; /* Allow thread to exit */
            }
        }
    }
}
