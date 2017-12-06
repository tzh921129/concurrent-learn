package net.jcip.examples;

/**
 * LeftRightDeadlock
 * <p>
 * Simple lock-ordering deadlock
 *
 * @author Brian Goetz and Tim Peierls
 */
public class LeftRightDeadlock {
    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight() throws InterruptedException {
        synchronized (left) {
            synchronized (right) {
                doSomething();
            }
        }
    }

    public void rightLeft() {
        synchronized (right) {
            synchronized (left) {
                doSomethingElse();
            }
        }
    }

    public void rightLeftAvailable() {
        synchronized (left) {
            synchronized (right) {
                doSomethingElse();
            }
        }
    }

    void doSomething() {
        System.out.println("left to right has done.");
    }

    void doSomethingElse() {
        System.out.println("right to left has done.");
    }

    public static void main(String[] args) throws InterruptedException {
        LeftRightDeadlock lock = new LeftRightDeadlock();
//        create two thread

        lock.leftRight();
//        the rightLeft() method may cause dead lock
//        lock.rightLeft();
        lock.rightLeftAvailable();
    }
}
