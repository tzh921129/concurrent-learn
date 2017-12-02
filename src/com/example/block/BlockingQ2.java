package com.example.block;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQ2 {
    private Object notEmpty = new Object();
    private Object notFull = new Object();
    private Queue<Object> linkedList = new LinkedList<Object>();
    private int maxLength = 10;

    public Object take() throws InterruptedException {
        synchronized (notEmpty) {
            if (linkedList.size() == 0) {
                notEmpty.wait();
            }
            synchronized (notFull) {
                if (linkedList.size() == maxLength) {
                    notFull.notifyAll();
                }
                return linkedList.poll();
            }
        }
    }

    public void offer(Object object) throws InterruptedException {
        synchronized (notEmpty) {
            if (linkedList.size() == 0) {
                notEmpty.notifyAll();
            }
            synchronized (notFull) {
                if (linkedList.size() == maxLength) {
                    notFull.wait();
                }
                linkedList.add(object);
            }
        }
    }
}
