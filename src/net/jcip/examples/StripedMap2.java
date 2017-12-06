package net.jcip.examples;

import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.List;

/**
 * StripedMap
 * <p/>
 * Hash-based map using lock striping
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class StripedMap2 {
    // Synchronization policy: buckets[n] guarded by locks[n%N_LOCKS]
    private static final int N_LOCKS = 16;
    private final List<Node> buckets;
    private final Object[] locks;

    private static class Node {
        Node next;
        Object key;
        Object value;

        private Node(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    public StripedMap2(int numBuckets) {
        buckets = new LinkedList<Node>();
        locks = new Object[N_LOCKS];
        for (int i = 0; i < N_LOCKS; i++) {
            locks[i] = new Object();
        }
    }

    private final int hash(Object key) {
        return Math.abs(key.hashCode() % buckets.size());
    }

    public Object get(Object key) {
        int hash = hash(key);
        synchronized (locks[hash % N_LOCKS]) {
            for (Node m = buckets.get(hash); m != null; m = m.next) {
                if (m.key.equals(key)) {
                    return m.value;
                }
            }
        }
        return null;
    }

    public void add(int index, Node node) {
        synchronized (locks[index % N_LOCKS]) {
            buckets.add(index, node);
            if(index==0){
                buckets.get(0).next=null;
            }else{
                buckets.get(index-1).next=node;
            }
        }
    }

    public void clear() {
        for (int i = 0; i < buckets.size(); i++) {
            synchronized (locks[i % N_LOCKS]) {
                buckets.set(i, null);
            }
        }
    }

    public static void main(String[] args) {
        StripedMap2 stripedMap = new StripedMap2(5);
        Node node1 = new Node("key1", "val1");
        Node node2 = new Node("key2", "val2");
        stripedMap.add(0,node1);
        stripedMap.add(1,node2);
        System.out.println();
    }
}
