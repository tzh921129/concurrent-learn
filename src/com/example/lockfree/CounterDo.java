package com.example.lockfree;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterDo {
    private AtomicInteger max = new AtomicInteger();

    //    循环 CAS 回退
    public void set(int value) {
        int current;
        do {
            current = max.get();
            if (value <= current) {
                break;
            }
        } while (!max.compareAndSet(current, value));
    }

    public int getMax() {
        return max.get();
    }
}
