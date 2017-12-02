package com.example.barrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class PerformanceTest {
    private int threadCount;
    private CyclicBarrier barrier;
    private int loopCount = 10;

    public PerformanceTest(int threadCount) {
        this.threadCount = threadCount;
        barrier = new CyclicBarrier(threadCount, new Runnable() {
            @Override
            public void run() {
                collectTestResult();
            }
        });
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread("test-thread" + i) {
                @Override
                public void run() {
                    for (int i = 0; i < loopCount; i++) {
                        doTest();
                        try {
                            barrier.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        }
    }

    private void doTest() {
        for(int i=0;i<10000;i++){

        }

    }

    private void collectTestResult() {
        System.out.println(Thread.currentThread().getName()+" loop done...");
    }

    public static void main(String[] args) {
        new PerformanceTest(5);
    }
}
