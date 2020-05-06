package com.wolf.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 使用Lock接口中lockInterruptibly()方法获取锁，另一个等待锁的线程响应中断
 * @Author wangqikang
 * @CreatedAt 2020-03-05 11:56
 **/
public class MyInterrupt {
    private Lock lock = new ReentrantLock();

    public void insert(Thread thread) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            System.out.println(thread.getName() + " 得到了锁");
            long startTime = System.currentTimeMillis();
            for (; ; ) {
                if (System.currentTimeMillis() - startTime >= Integer.MAX_VALUE) {
                    break;
                }
            }
        } finally {
            System.out.println(Thread.currentThread().getName() + " 执行finally");
            lock.unlock();
            System.out.println(thread.getName() + " 释放了锁");
        }
    }

    public static void main(String[] args) {
        MyInterrupt inter = new MyInterrupt();
        MyThread thread1 = new MyThread(inter);
        MyThread thread2 = new MyThread(inter);
        thread1.start();
        thread2.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread1.interrupt();//另一个线程一直占用锁，当前线程调用方法响应中断
        System.out.println("========================");
    }

    static class MyThread extends Thread {
        private MyInterrupt inter;

        public MyThread(MyInterrupt inter) {
            this.inter = inter;
        }

        @Override
        public void run() {
            try {
                inter.insert(Thread.currentThread());
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + " 被中断");
            }
        }
    }
}
