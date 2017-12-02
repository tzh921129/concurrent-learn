package com.example.lockfree;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    private AtomicInteger max = new AtomicInteger();
    //    循环 CAS 回退
    public void set(int value) {
        for (; ; ) {//循环
            int current = max.get();
            if (value > current) {
                if (max.compareAndSet(current, value)) {//CAS
                    break;//回退
                } else {
                    continue;
                }
            } else {
                break;
            }
        }
    }

    public int getMax() {
        return max.get();
    }
}
