package com.wolf.bigdata.enhance.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 等待的线程被中断
 * @Author wangqikang
 * @Date 2019-09-03 08:30
 */
public class MyInterruptibly {
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
        MyInterruptibly inter = new MyInterruptibly();
        MyThread thread1 = new MyThread(inter);
        MyThread thread2 = new MyThread(inter);
        thread1.start();
        thread2.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread1.interrupt();
        System.out.println("========================");
    }

    static class MyThread extends Thread {
        private MyInterruptibly inter;

        public MyThread(MyInterruptibly inter) {
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
