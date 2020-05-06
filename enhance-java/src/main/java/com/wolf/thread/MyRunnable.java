package com.wolf.thread;

/**
 * @Description 通过实现Runnable接口实现线程
 * @Author wangqikang
 * @CreatedAt 2020-03-04 22:26
 **/
public class MyRunnable implements Runnable {
    private String flag;

    public MyRunnable(String flag) {
        this.flag = flag;
    }

    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("线程 " + threadName + " 的 run 方法被调用 ... ");

        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(100);
                System.out.println(threadName + " ... " + flag + " ... " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(new MyRunnable("a"));
        Thread thread2 = new Thread(new MyRunnable("b"));
        thread1.start();
        thread2.start();
    }
}
