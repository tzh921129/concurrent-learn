package net.jcip.examples;

/**
 * ThisEscape
 * <p/>
 * Implicitly allowing the this reference to escape
 * 不要让this在构造期间溢出
 * 不要在构造方法中启动线程，或者覆写方法
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ThisEscape {
    public ThisEscape(EventSource source) {
        source.registerListener(new EventListener() {
            @Override
            public void onEvent(Event e) {
                doSomething(e);
            }
        });
    }

    void doSomething(Event e) {
    }


    interface EventSource {
        void registerListener(EventListener e);
    }

    interface EventListener {
        void onEvent(Event e);
    }

    interface Event {
    }
}

