package com.wolf.bigdata.enhance.synchronize;

/**
 * @Description synchronized 关键字
 * @Author wangqikang
 * @Date 2019-09-03 07:55
 */
public class MySynchronized {

    public static void main(String[] args) {
        final MySynchronized mySynchronized1 = new MySynchronized();
        final MySynchronized mySynchronized2 = new MySynchronized();

        new Thread("thread1") {
            @Override
            public void run() {
                synchronized ("sb") {
                    try {
                        System.out.println(this.getName() + " start ");
                        // 若发生异常，JVM自动释放锁
//                        int i = 1 / 0;
                        Thread.sleep(5000);
                        System.out.println(this.getName() + " 醒了 ");
                        System.out.println(this.getName() + " end ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        new Thread("thread2") {
            @Override
            public void run() {
                // 争夺同一把锁，线程1没有释放之前，线程2只能等待
                synchronized ("sb") {
                    // 如果不是同一把锁，则同时执行
//                synchronized ("bs") {
                    System.out.println(this.getName() + " start ");
                    System.out.println(this.getName() + " end ");
                }
            }
        }.start();
    }
}
