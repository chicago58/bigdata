package com.wolf.bigdata.enhance.thread;


/**
 * @Description
 * @Author wangqikang
 * @Date 2019-09-02 22:50
 */
public class ThreadWithImplements implements Runnable {

    private String flag;

    public ThreadWithImplements(String flag) {
        this.flag = flag;
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(new ThreadWithImplements("a"), "thread-a");
        Thread thread2 = new Thread(new ThreadWithImplements("a"), "thread-b");
        thread1.start();
        thread2.start();
    }

    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("线程 " + threadName + " 的 run 方法被调用 ... ");

        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(100);
                System.out.println(threadName + " ... " + flag + " ... " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
