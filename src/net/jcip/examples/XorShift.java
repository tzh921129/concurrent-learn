package net.jcip.examples;

import java.util.concurrent.atomic.*;

/**
 * XorShift
 * 适用于中等品质的随机数生成器
 *
 * @author Brian Goetz and Tim Peierls
 */
public class XorShift {
    static final AtomicInteger seq = new AtomicInteger(8862213);
    int x = -1831433054;

    public XorShift(int seed) {
        x = seed;
    }

    public XorShift() {
        this((int) System.nanoTime() + seq.getAndAdd(129));
    }

    public int next() {
        x ^= x << 6;
        x ^= x >>> 21;
        x ^= (x << 7);
        return x;
    }
}
