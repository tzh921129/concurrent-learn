package com.example.communicate;

import java.util.concurrent.*;

/**
 * @author tzh92
 */
public class ExecutorTest {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
//      Task executor
        Callable<Object> task = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Object result = "returned Value";
                return result;
            }
        };
//      Task Submitter
        Future<Object> future = executor.submit(task);
//        future.get();
        future.get(3, TimeUnit.SECONDS);
    }
}
