package net.jcip.examples;

import net.jcip.annotations.ThreadSafe;

/**
 * BoundedBuffer
 * <p/>
 * Bounded buffer using condition queues
 * 只有同时满足下列条件后，才能用单一的Notify取代Nofifyall
 * (1）相同的等待者，只有一个条件为此与条件队列相关，每个线程从wait返回后
 * 执行相同的逻辑，并且;
 * (2)一进一出，一个对条件变量的通知，至多只能激活一个线程
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class BoundedBuffer<V> extends BaseBoundedBuffer<V> {
    // CONDITION PREDICATE: not-full (!isFull())
    // CONDITION PREDICATE: not-empty (!isEmpty())
    public BoundedBuffer() {
        this(100);
    }

    public BoundedBuffer(int size) {
        super(size);
    }

    // BLOCKS-UNTIL: not-full
    public synchronized void put(V v) throws InterruptedException {
        while (isFull()) {
            wait();
        }
        doPut(v);
        notifyAll();
    }

    // BLOCKS-UNTIL: not-empty
    public synchronized V take() throws InterruptedException {
        while (isEmpty()) {
            wait();
        }
        V v = doTake();
        notifyAll();
        return v;
    }

    // BLOCKS-UNTIL: not-full
    // Alternate form of put() using conditional notification
    public synchronized void alternatePut(V v) throws InterruptedException {
        while (isFull()) {
            wait();
        }
        boolean wasEmpty = isEmpty();
        doPut(v);
        if (wasEmpty) {
            notifyAll();
        }
    }

    public synchronized V alternateTake() throws InterruptedException {
        while (isEmpty()) {
            wait();
        }
        boolean wasFull = isFull();
        V v = doTake();
        if (wasFull) {
            notifyAll();
        }
        return v;
    }
}
