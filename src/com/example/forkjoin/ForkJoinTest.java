package com.example.forkjoin;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoinTest {
    public static void main(String[] args) {
        final ForkJoinPool mainPool = new ForkJoinPool();
        int len = 1000 * 1000 * 10;
        int[] array = new int[len];
        mainPool.invoke(new SortTask(array, 0, len - 1));
    }

    public static class SortTask extends RecursiveAction {
        private int[] array;
        private int fromindex;
        private int toindex;
        private final int chunksize = 1024;

        public SortTask(int[] array, int fromindex, int toindex) {
            this.array = array;
            this.fromindex = fromindex;
            this.toindex = toindex;
        }

        @Override
        protected void compute() {
            int size = toindex - fromindex + 1;
            if (size < chunksize) {
                Arrays.sort(array, fromindex, toindex);
            } else {
                int leftSize = size / 2;
                SortTask leftTask = new SortTask(array, fromindex, fromindex + leftSize);
                SortTask rightTask = new SortTask(array, fromindex + leftSize + 1, toindex);
                invokeAll(leftTask, rightTask);
            }
        }
    }
}
