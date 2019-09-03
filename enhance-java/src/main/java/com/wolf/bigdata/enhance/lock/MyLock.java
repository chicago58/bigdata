package com.wolf.bigdata.enhance.lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description Lock接口中lock()方法
 * @Author wangqikang
 * @Date 2019-09-03 08:17
 */
public class MyLock {
    private static ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                Thread thread = Thread.currentThread();

                lock.lock();
                try {
                    System.out.println(thread.getName() + " 得到了锁 ");
                    for (int i = 0; i < 5; i++) {
                        arrayList.add(i);
                    }
                } finally {
                    System.out.println(thread.getName() + " 释放了锁");
                    lock.unlock();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                Thread thread = Thread.currentThread();
                lock.lock();

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
        }.start();
    }
}
