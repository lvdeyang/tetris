package com.sumavision.bvc.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>线程池相关
 *
 * @author 
 */
@Slf4j
public final class ThreadPool {

    private static ThreadPoolExecutor threadPool = null;

    private static ScheduledThreadPoolExecutor scheduledExecutor = null;

    static {
        threadPool = new ThreadPoolExecutor(128, 256, 1L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(1024),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        threadPool.allowCoreThreadTimeOut(true);

        scheduledExecutor = new ScheduledThreadPoolExecutor(256);
    }

    private ThreadPool() {
    }

    public static ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    public static ScheduledThreadPoolExecutor getScheduledExecutor() {
        return scheduledExecutor;
    }

    /**
     * 
     * <p>
     * <b>异步执行</b>
     * <p>
     * <pre>
     * 在线程中异步执行方法
     * </pre>
     *
     * @param object    执行实体
     * @param method    执行方法
     * @param objects   执行参数
     */
    public static void async(final Object object, final Method method, final Object... objects) {
        if (null == method) {
            return;
        }

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    method.invoke(object, objects);
                } catch (IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
                    log.error("Method Invoke Error.", e);
                }
            }
        });

    }

}
