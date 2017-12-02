package com.example.communicate;

import java.util.concurrent.*;

public class ScheduleTest {
    public static void main(String[] args) throws Exception {
        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(2);
        Callable<Object> callable = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println(Thread.currentThread().getName() + "...");
                return "result";
            }
        };
        ScheduledFuture<Object> future = schedule.schedule(callable, 2, TimeUnit.SECONDS);
        System.out.println(future.get());
    }
}
