package com.sumavision.signal.bvc.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MqExecutorService {

	private ConcurrentHashMap<String, Future> futureMap;
	 
    private ScheduledExecutorService executorService;
    
    private MqExecutorService(){
    	try {
			this.executorService = new ScheduledThreadPoolExecutor(16);
			this.futureMap = new ConcurrentHashMap<String, Future>();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private static MqExecutorService instance;
    
    public static MqExecutorService getInstance(){
    	if(instance == null){
    		instance = new MqExecutorService();
    	}
    	return instance;
    }
    
    /**
     * 执行周期进程<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年6月16日 上午11:07:43
     * @param Runnable runnable 进程
     * @param String name 进程标识名
     */
    public void sheduler(Runnable runnable, String name) {
    	synchronized (this) {
        	if (runnable != null) {
        		Future existFuture = futureMap.get(name);
        		if(existFuture == null){
        			Future future = executorService.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS);
                    futureMap.put(name, future);
        		}else {
        			//中断线程
        			existFuture.cancel(true);
        			Future future = executorService.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS);
        			futureMap.put(name, future);
    			}
            }
		}
    }
    
    /**
     * 强制终止定时线程
     */
    public void terminal(String key) {
        try {
            Future future = futureMap.get(key);
            if(future != null){
            	futureMap.remove(key);
                future.cancel(true);
            }
        } finally {
            System.out.println("线程：Future jobId " + key + " had cancel");
        }
    }
	
}
