package com.sailing.reflex.threadlocal;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
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
    private ThreadPoolExecutor createThreadPool(int capacity){
        // RejectedExecutionHandler handler 该对象用于处理，达到现场池对象边界时的一些处理手段
        // 通常的做法有这几种：一、直接抛出异常用以说明当前线程池处理的现场已经满负荷。二、自定义排队等待模式，多长时间后再来尝试给线程池里头添加
        return new ThreadPoolExecutor(30,50,0,
                        TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(capacity), new ConsumerHandler());
    }

    /**
     * 当现场池中的现场数量达到预设值时，触发线程等待机制
     */
    public class ConsumerHandler implements RejectedExecutionHandler{

        /**
         * 线程池决绝添加线程后的处理机制，采用排队等待
         * @param r 线程对象
         * @param executor
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            boolean wait = false;
            while (!wait){
                try {
                    // 处理添加不进去异常的问题
                    // 添加不进去2秒后在尝试添加
                    wait = executor.getQueue().add(r);
                } catch (Exception e) {
                    wait = false;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    log.info("处理添加不进去异常的问题,添加不进去2秒后在尝试添加：>{}", e.fillInStackTrace());
                }
            }
        }
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
            int[] temp = new int[1 * 1024 * 1024];
            Map mapData = new HashMap();
            mapData.put(Thread.currentThread().getName() + "<>" + Thread.currentThread().getId(), temp);
            alarmThreadLocalData.set(mapData);

            log.info("高并发场景应用测试，{}", alarmThreadLocalData.get());

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
     * @param num 线程数量
     * @param capacity 线程池队列容量
     * @author: LIULEI
     */
    public void attemptPrint(int num,int capacity){
        ThreadPoolExecutor threadPoolExecutor = this.createThreadPool(capacity);
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