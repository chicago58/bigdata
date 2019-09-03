package com.wolf.bigdata.enhance.readwritelock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Description 读写锁实现读写分离，读操作能够并发进行，写操作锁定单个线程
 * @Author wangqikang
 * @Date 2019-09-03 22:55
 */
public class MyReentrantReadWriteLock {
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        final MyReentrantReadWriteLock reentrantReadWriteLock = new MyReentrantReadWriteLock();

        new Thread() {
            @Override
            public void run() {
                reentrantReadWriteLock.get(Thread.currentThread());
                reentrantReadWriteLock.write(Thread.currentThread());
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                reentrantReadWriteLock.get(Thread.currentThread());
                reentrantReadWriteLock.write(Thread.currentThread());
            }
        }.start();
    }

    /**
     * 读操作
     *
     * @param thread
     */
    public void get(Thread thread) {
        rw.readLock().lock();

        try {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start <= 1000) {
                System.out.println(thread.getName() + " 正在进行读操作！");
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {

        } finally {
            rw.readLock().unlock();
        }
    }

    /**
     * 写操作
     *
     * @param thread
     */
    public void write(Thread thread) {
        rw.writeLock().lock();

        try {
            long start = System.currentTimeMillis();

            while (System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName() + " 正在进行写操作！");
            }
            System.out.println(thread.getName() + " 写操作完成！");
        } finally {
            rw.writeLock().unlock();
        }
    }
}
