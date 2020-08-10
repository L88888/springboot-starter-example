package com.sailing.reflex.threadlocal;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @program: es-spring-boot-starter-pom
 * @description: ThreadLocal 线程内部数据存储应用
 * @author: LIULEI
 * @create: 2020-08-03 14:25:
 **/
@Slf4j
public class SThreadLocal {

    /**
     * 每个线程之间独立存储数据的方式,线程间数据隔离
     * 弱引用会引起内存泄露
     * @author: LIULEI
     */
    private ThreadLocal<Map> threadLocal = new ThreadLocal<>();

    /**
     * ThreadLocal 线程间数据隔离测试
     * @author: LIULEI
     */
    public void connSession(){
        // 采用j8新特性Stream直接创建5个线程
        IntStream.range(0,5).forEach(i -> new Thread(()->{
            Map tempMap = new HashMap();
            tempMap.put(i, i + 1000);
            threadLocal.set(tempMap);
            log.info("当前线程与local值为:>{}", threadLocal.get());
            // 防止内存泄露,每次使用完ThreadLocal中的数据之后就删除掉对应的key
//            threadLocal.remove();
            if (false){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start());
    }

    /**
     * 初始化线程池
     * ThreadLocalPool
     * @author: LIULEI
     */
    private ThreadPoolExecutor createThreadPool(){
        return new ThreadPoolExecutor(5,5,0,
                        TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 定义一个多线程的预警分发实例
     * @author: LIULEI
     */
    public class AlarmApp implements Runnable{

        /**
         * 对线程之间数据隔离
         */
        ThreadLocal<Map> alarmThreadLocalData = new ThreadLocal();

        @Override
        public void run() {
            // 声明一个2M的数据缓存块
            int[] temp = new int[2 * 1024 * 1024];
            Map mapData = new HashMap();
            mapData.put(Thread.currentThread().getName() + "<>" + Thread.currentThread().getId(), temp);
            alarmThreadLocalData.set(mapData);

            log.info("{}", alarmThreadLocalData.get());

            /**
             * 如果去掉下面的注释就可以正常运行
             * 因为remove内部会将ThreadLocalMap中key为null的value清除
             * 这也就解释了为什么key是用弱引用修饰了,当key(ThreadLocal)被废弃的时候,可以回收掉当前ThreadLocalMap的Key
             */
            alarmThreadLocalData.remove();
        }
    }

    /**
     * 尝试输出ThreadLocal线程间数据隔离存储方法
     * @author: LIULEI
     */
    public void attemptPrint(int num){
        ThreadPoolExecutor threadPoolExecutor = this.createThreadPool();
        for (int i =0;i < num;i++){
//            try {
                // 暂停一秒
//                TimeUnit.SECONDS.sleep(1);
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            log.info("num value >{}", i);
            // 将线程加入线程池中
            threadPoolExecutor.execute(new AlarmApp());
        }
        threadPoolExecutor.shutdown();
    }
}