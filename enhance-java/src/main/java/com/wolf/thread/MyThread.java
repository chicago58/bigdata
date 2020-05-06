package com.wolf.thread;

import java.util.Random;

/**
 * @Description 通过继承Thread类实现线程
 * @Author wangqikang
 * @CreatedAt 2020-03-04 22:14
 **/
public class MyThread extends Thread {
    private Random random = new Random();

    private String flag;

    public MyThread(String flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();// 获取当前线程名称
        System.out.println(threadName + " 线程的 run 方法被调用 ... ");

        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(random.nextInt(10) * 100);
                System.out.println(threadName + " ... " + flag + " ... " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MyThread thread1 = new MyThread("a");
        MyThread thread2 = new MyThread("b");
        thread1.start();
        thread2.start();
    }
}
