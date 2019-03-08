package com.sumavision.tetris.mvc.ext.context;

import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;

/**
 * 线程内http session缓存<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月6日 上午10:39:26
 */
public class HttpSessionThreadLocal {

	private static final ConcurrentHashMap<Thread, HttpSession> context = new ConcurrentHashMap<Thread, HttpSession>();
	
	/**
	 * 缓存一个线程和session的映射<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 上午10:07:50
	 * @param Thread thread 当前线程
	 * @param HttpSession session session
	 */
	public static void put(Thread thread, HttpSession session){
		context.put(thread, session);
	}
	
	/**
	 * 获取当前线程的session<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 上午10:08:35
	 * @param Thread thread 当前线程
	 */
	public static HttpSession get(Thread thread){
		return context.get(thread);
	}
	
	/**
	 * 删除当前线程的session<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 上午10:09:56
	 * @param Thread thread 当前线程
	 */
	public static void remove(Thread thread){
		context.remove(thread);
	}
	
}
