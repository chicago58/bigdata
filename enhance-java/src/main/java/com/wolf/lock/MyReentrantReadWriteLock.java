package com.wolf.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Description 多个线程读操作并发执行，写操作竞争锁资源
 * @Author wangqikang
 * @CreatedAt 2020-03-05 13:27
 **/
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
