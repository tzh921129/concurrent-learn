package com.example.daemon;

import java.io.IOException;

public class DaemonTest extends Thread {
    public DaemonTest(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; ; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(i);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DaemonTest daemonTest = new DaemonTest("测试精灵线程");
        //由于测试精灵会一直运行下去，所以setDaemon()注释掉的化程序不会结束
        daemonTest.setDaemon(true);
        daemonTest.start();
        //不会执行下去了
//        daemonTest.join();
        // 设置为true，即可主线程结束,精灵线程也结束
        System.out.println("isDaemon = " + daemonTest.isDaemon());
        try {
            System.in.read();
        } catch (IOException ex) {

        }
    }
}
