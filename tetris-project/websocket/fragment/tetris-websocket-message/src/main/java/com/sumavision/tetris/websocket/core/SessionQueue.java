package com.sumavision.tetris.websocket.core;

import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

/**
 * session 队列<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年9月10日 下午3:52:53
 */
public class SessionQueue {

	private static SessionQueue instance;
	
	public static SessionQueue getInstance(){
		synchronized(SessionQueue.class){
			if(instance == null) instance = new SessionQueue();
		}
		return instance;
	}
	
	private final ConcurrentHashMap<String, Session> queue = new ConcurrentHashMap<String, Session>();
	
	/**
	 * 注册websocket session<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月10日 下午4:34:21
	 * @param Long userId 用户id
	 * @param Session session websocket session
	 */
	public void put(Long userId, Session session){
		this.queue.put(userId.toString(), session);
	}
	
	/**
	 * 注册websocket session<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月10日 下午4:34:21
	 * @param String userId 用户id
	 * @param Session session websocket session
	 */
	public void put(String userId, Session session){
		this.queue.put(userId, session);
	}
	
	/**
	 * 获取用户session<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月10日 下午4:35:28
	 * @param Long userId 
	 * @return Session websocket session
	 */
	public Session get(Long userId){
		return this.queue.get(userId.toString());
	}
	
	/**
	 * 获取用户session<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月10日 下午4:35:28
	 * @param String userId 
	 * @return Session websocket session
	 */
	public Session get(String userId){
		return this.queue.get(userId);
	}
	
	/**
	 * 移除session<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月10日 下午4:38:41
	 * @param Long userId 用户id
	 * @return Session websocket session
	 */
	public Session remove(Long userId){
		return this.queue.remove(userId.toString());
	}
	
	/**
	 * 移除session<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月10日 下午4:38:41
	 * @param String userId 用户id
	 * @return Session websocket session
	 */
	public Session remove(String userId){
		return this.queue.remove(userId);
	}
	
}
