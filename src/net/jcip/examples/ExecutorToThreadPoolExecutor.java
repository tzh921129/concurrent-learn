package net.jcip.examples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ExecutorToThreadPoolExecutor {
    private final ExecutorService exec;
//    private final ExecutorService unconfigExec;

    public ExecutorToThreadPoolExecutor() {
        exec = Executors.newCachedThreadPool();
//        unconfigExec = Executors.unconfigurableExecutorService(exec);
        init();
    }

    private void init() {
        if (exec instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor) exec).setCorePoolSize(10);
        } else {
            throw new AssertionError("Opops,bad assumptions");
        }
        /*if(unconfigExec instanceof ThreadPoolExecutor){
            ((ThreadPoolExecutor)unconfigExec).setCorePoolSize(10);
        }else{
            throw new AssertionError("Opops,bad assumptions");
        }*/
    }

    public static void main(String[] args) {
        ExecutorToThreadPoolExecutor executor = new ExecutorToThreadPoolExecutor();
        System.out.println(executor);
    }
}
