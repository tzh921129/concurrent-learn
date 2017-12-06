package net.jcip.examples;

import junit.framework.TestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PutTakeTest
 * <p/>
 * Producer-consumer test program for BoundedBuffer
 *
 * @author Brian Goetz and Tim Peierls
 */
public class PutTakeTestCountDownLatch extends TestCase {
    protected static final ExecutorService pool = Executors.newCachedThreadPool();
    protected CountDownLatch start;
    protected CountDownLatch end;
    protected final SemaphoreBoundedBuffer<Integer> bb;
    protected final int nTrials, nPairs;
    protected final AtomicInteger putSum = new AtomicInteger(0);
    protected final AtomicInteger takeSum = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        new PutTakeTestCountDownLatch(10, 10, 100000).test(); // sample parameters
        pool.shutdown();
    }

    public PutTakeTestCountDownLatch(int capacity, int npairs, int ntrials) {
        this.bb = new SemaphoreBoundedBuffer<Integer>(capacity);
        this.nTrials = ntrials;
        this.nPairs = npairs;
        this.start = new CountDownLatch(2 * capacity);
        this.end = new CountDownLatch(2 * capacity);
    }

    void test() {
        try {
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            start.await(); // wait for all threads to be ready
            end.await(); // wait for all threads to finish
            assertEquals(putSum.get(), takeSum.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

    class Producer implements Runnable {
        @Override
        public void run() {
            try {
                int seed = (this.hashCode() ^ (int) System.nanoTime());
                int sum = 0;
                start.countDown();
                for (int i = nTrials; i > 0; --i) {
                    bb.put(seed);
                    sum += seed;
                    seed = xorShift(seed);
                }
                putSum.getAndAdd(sum);
                end.countDown();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                start.countDown();
                int sum = 0;
                for (int i = nTrials; i > 0; --i) {
                    sum += bb.take();
                }
                takeSum.getAndAdd(sum);
                end.countDown();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
