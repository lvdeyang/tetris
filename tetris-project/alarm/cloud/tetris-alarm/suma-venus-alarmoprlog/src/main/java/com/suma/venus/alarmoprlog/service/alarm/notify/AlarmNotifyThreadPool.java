package com.suma.venus.alarmoprlog.service.alarm.notify;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 告警通知的线程池 单例模式
 * 
 * @author chenmo
 *
 */
public class AlarmNotifyThreadPool {

	private static ThreadPoolExecutor threadPoolExecutor = null;

	private AlarmNotifyThreadPool() {
	}

	public static ThreadPoolExecutor getThreadPool() {
		if (threadPoolExecutor == null) {
			threadPoolExecutor = new ThreadPoolExecutor(20, 256, 10L, TimeUnit.SECONDS,
					new LinkedBlockingDeque<Runnable>(256), new ThreadPoolExecutor.DiscardOldestPolicy());

			threadPoolExecutor.allowCoreThreadTimeOut(true);

		}
		return threadPoolExecutor;
	}

}
