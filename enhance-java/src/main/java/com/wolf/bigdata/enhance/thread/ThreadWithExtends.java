package com.wolf.bigdata.enhance.thread;

import java.util.Random;

/**
 * @Description
 * @Author wangqikang
 * @Date 2019-09-02 22:50
 */
public class ThreadWithExtends extends Thread {

    private String flag;

    public ThreadWithExtends(String flag) {
        this.flag = flag;
    }

    public static void main(String[] args) {
        Thread thread1 = new ThreadWithExtends("a");
        Thread thread2 = new ThreadWithExtends("b");
        thread1.start();
        thread2.start();
    }

    @Override
    public void run() {
        // 获取当前线程名称
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " 线程的 run 方法被调用 ... ");

        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(random.nextInt(10) * 100);
                System.out.println(threadName + " ... " + flag + " ... " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

