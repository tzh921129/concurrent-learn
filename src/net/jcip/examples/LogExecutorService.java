package net.jcip.examples;

import net.jcip.annotations.GuardedBy;

import java.io.PrintWriter;
import java.util.concurrent.*;

/**
 * @author tzh92
 */
public class LogExecutorService {
    private final ExecutorService exec = Executors.newSingleThreadExecutor();
    private static final long TIMEOUT = 1000;
    private final PrintWriter writer;
    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    @GuardedBy("this")
    private boolean isShutdown;

    public LogExecutorService(PrintWriter writer) {
        this.writer = writer;
        queue = new LinkedBlockingQueue<String>();
        loggerThread = new LoggerThread();
    }

    public void start() {
        loggerThread.start();
    }

    public void stop() throws InterruptedException {
        try {
            exec.shutdown();
            exec.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
        } finally {
            writer.close();
        }
    }

    public void log(String msg) {
        try {
            exec.execute(new WriteTask(msg));
        } catch (RejectedExecutionException ignored) {

        }
    }

    private class WriteTask implements Runnable {
        private final String msg;

        public WriteTask(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                queue.put(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class LoggerThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        synchronized (LogExecutorService.this) {
                            if (isShutdown) {
                                break;
                            }
                        }
                        String msg = queue.take();
                        writer.println(msg);
                    } catch (InterruptedException e) { /* retry */
                    }
                }
            } finally {
                writer.close();
            }
        }
    }
}
