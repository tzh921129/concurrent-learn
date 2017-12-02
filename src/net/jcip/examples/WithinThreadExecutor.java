package net.jcip.examples;

import java.util.concurrent.Executor;

/**
 * WithinThreadExecutor
 * <p/>
 * Executor that executes tasks synchronously in the calling thread
 *
 * @author Brian Goetz and Tim Peierls
 */
public class WithinThreadExecutor implements Executor {
    @Override
    public void execute(Runnable r) {
        r.run();
    }

    ;
}
