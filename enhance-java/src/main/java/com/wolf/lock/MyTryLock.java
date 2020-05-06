package com.wolf.lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 使用Lock接口中tryLock()方法获取锁。一个线程获取锁后，另一个线程获取不到，不会一直等待
 * @Author wangqikang
 * @CreatedAt 2020-03-05 10:55
 **/
public class MyTryLock {
    private static ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private static Lock lock = new ReentrantLock();

    public static void threadHandler() {
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
//                lock.unlock();
//                System.out.println(thread.getName() + " 释放了锁");

                for (Integer arr : arrayList) {
                    System.out.print(arr + " ");
                }
                System.out.println();

            }
        }
    }

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                threadHandler();
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                threadHandler();
            }
        }.start();
    }
}
