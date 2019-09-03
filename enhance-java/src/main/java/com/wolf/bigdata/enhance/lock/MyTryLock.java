package com.wolf.bigdata.enhance.lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 一个线程获取到锁，另一个线程获取不到锁，不会一直等待
 * @Author wangqikang
 * @Date 2019-09-03 08:23
 */
public class MyTryLock {
    private static ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                Thread thread = Thread.currentThread();
                boolean tryLock = lock.tryLock();
                System.out.println(thread.getName() + " " + tryLock);

                if (tryLock) {
                    try {
                        System.out.println(thread.getName() + " 得到了锁");
                        for (int i = 0; i < 5; i++) {
                            arrayList.add(i);
                        }
                    } finally {
                        System.out.println(thread.getName() + " 释放了锁");
                        lock.unlock();
                    }
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                Thread thread = Thread.currentThread();
                boolean tryLock = lock.tryLock();
                System.out.println(thread.getName() + " " + tryLock);

                if (tryLock) {
                    try {
                        System.out.println(thread.getName() + "得到了锁");
                        for (int i = 0; i < 5; i++) {
                            arrayList.add(i);
                        }
                    } finally {
                        System.out.println(thread.getName() + "释放了锁");
                        lock.unlock();
                    }
                }
            }
        }.start();
    }
}
