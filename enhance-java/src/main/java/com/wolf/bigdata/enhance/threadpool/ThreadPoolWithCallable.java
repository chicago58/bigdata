package com.wolf.bigdata.enhance.threadpool;

import java.util.concurrent.*;

/**
 * @Description 使用线程池来执行线程
 * @Author wangqikang
 * @Date 2019-09-04 07:47
 */
public class ThreadPoolWithCallable {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 获取CPU核数
        int coreNum = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(coreNum);

        for (int i = 0; i < 10; i++) {
            /**
             * Runnable 与 Callable 的区别：
             * Runnable的run()方法没有返回结果，主线程无法获得任务线程的返回值；
             * Callable的call()方法可以返回结果，但是主线程被阻塞，一直等到任务线程返回才能获取结果
             */
            Future<String> submit = executor.submit(new Callable<String>() {
                public String call() throws Exception {
                    System.out.println(Thread.currentThread().getName() + " --> 正在工作");
                    Thread.sleep(5000);
                    System.out.println(Thread.currentThread().getName() + " --> 结束工作");
                    return Thread.currentThread().getName();
                }
            });

            // 从Feature中获取结果，该方法会被阻塞，一直等到任务线程结束才能返回结果
            System.out.println("获取结果：" + submit.get());
        }

        executor.shutdown();
    }
}
