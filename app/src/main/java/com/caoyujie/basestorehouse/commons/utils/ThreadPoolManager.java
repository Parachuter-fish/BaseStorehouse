package com.caoyujie.basestorehouse.commons.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by caoyujie on 16/12/14.
 * 线程池
 */
public class ThreadPoolManager {
    //private static final int CPU_COUNT = DeviceUtils.getCPUCount();     //cpu数量
    //private static final int CORE_POOL_SIZE = CPU_COUNT + 1;            //计算密集型建议线程数：N + 1
    //private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;         //IO密集型线程数： 2N + 1
    private static ThreadPoolManager INSTANCE;
    private ExecutorService threadPool;

    private ThreadPoolManager(){
        threadPool = Executors.newCachedThreadPool();
    }

    public static ThreadPoolManager getInstance(){
        if(INSTANCE == null){
            synchronized (ThreadPoolManager.class){
                if(INSTANCE == null){
                    INSTANCE = new ThreadPoolManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 执行某线程任务
     */
    public void execute(Runnable runnable){
        if(threadPool != null){
            threadPool.execute(runnable);
        }
    }

    /**
     * 停止线程池中的线程
     * 调用之后，线程池中不允许再加入新的线程，但已有的线程会等他们支持完毕之后关闭
     */
    public void shutdown(){
        if(threadPool != null && !threadPool.isShutdown()){
            threadPool.shutdown();
        }
    }

    /**
     * 停止线程池中的线程
     * 调用之后，线程池中不允许再加入新的线程，并立即停止正在执行的线程(不确定立马就能销毁)
     */
    public void shutdownNow(){
        if(threadPool != null && !threadPool.isShutdown()){
            threadPool.shutdownNow();
        }
    }

}
