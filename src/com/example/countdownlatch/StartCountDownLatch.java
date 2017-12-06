package com.example.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class StartCountDownLatch {
    final static CountDownLatch startLatch = new CountDownLatch(1);

    public void test() {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread("worker thread " + i) {
                @Override
                public void run() {
                    try {
                        startLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    waitToStart();
                }
            };
            thread.start();
        }
    }

    public void waitToStart() {
        System.out.println(Thread.currentThread().getName() + " start");
    }

    public static void main(String[] args) throws InterruptedException {
        new StartCountDownLatch().test();
        startLatch.countDown();
    }

}
