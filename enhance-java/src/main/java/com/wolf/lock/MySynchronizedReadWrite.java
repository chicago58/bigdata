package com.wolf.lock;

/**
 * @Description synchronized实现读写操作，多个线程竞争锁资源依次执行
 * @Author wangqikang
 * @CreatedAt 2020-03-05 13:17
 **/
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
