package com.wolf.bigdata.enhance.readwritelock;

/**
 * @Description 一个线程的读写操作，使用 synchronized 实现，读写操作只能锁住后每个线程依次执行
 * @Author wangqikang
 * @Date 2019-09-03 22:54
 */
public class MySynchronizedReadWrite {

    public static void main(String[] args) {
        final MySynchronizedReadWrite synchronizedReadWrite = new MySynchronizedReadWrite();

        new Thread() {
            @Override
            public void run() {
                synchronizedReadWrite.operate(Thread.currentThread());
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                synchronizedReadWrite.operate(Thread.currentThread());
            }
        }.start();
    }

    public synchronized void operate(Thread thread) {
        long start = System.currentTimeMillis();
        int i = 0;
        while (System.currentTimeMillis() - start <= 1) {
            i++;
            if (i % 4 == 0) {
                System.out.println(thread.getName() + " 正在进行写操作！" + i);
            } else {
                System.out.println(thread.getName() + " 正在进行读操作！" + i);
            }
        }
        System.out.println(thread.getName() + " 读写操作完成！");
    }
}
