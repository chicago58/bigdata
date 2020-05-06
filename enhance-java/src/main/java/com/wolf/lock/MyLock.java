package com.wolf.lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 使用Lock接口中lock()方法获取锁
 * @Author wangqikang
 * @CreatedAt 2020-03-05 10:43
 **/
public class MyLock {
    private static ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private static Lock lock = new ReentrantLock();

    public static void handler() {
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

            for (Integer i : arrayList) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                handler();
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                handler();
            }
        }.start();
    }
}
