package com.example.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class CompleteCountDownLatch {
    private final int count;
    private final CountDownLatch completeLatch;

    public CompleteCountDownLatch(int count) {
        this.count = count;
        this.completeLatch = new CountDownLatch(count);
    }

    public void test() throws InterruptedException {
        for (int i = 0; i < count; i++) {
            Thread thread = new Thread("worker thread " + i) {
                @Override
                public void run() {
                    done();
                    completeLatch.countDown();
                }
            };
            thread.start();
        }
        //等待所有线程结束
        completeLatch.await();
        System.out.println("all threads has done.");
    }

    public void done() {
        System.out.println(Thread.currentThread().getName() + " has done.");
    }

    public static void main(String[] args) throws InterruptedException {
        new CompleteCountDownLatch(10).test();
    }

}
