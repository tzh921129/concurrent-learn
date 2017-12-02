package com.example.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class MyCountDownLatch {
    final static CountDownLatch completeLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread("worker thread " + i) {
                @Override
                public void run() {
                    completeLatch.countDown();
                }
            };
            thread.start();
        }
        completeLatch.await();
    }

}
