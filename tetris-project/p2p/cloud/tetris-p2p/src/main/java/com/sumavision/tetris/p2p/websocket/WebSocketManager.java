package com.sumavision.tetris.p2p.websocket;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

/**
 * websocket管理器<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月10日 上午9:08:57
 */
public class WebSocketManager {

	private ConcurrentHashMap<String, Session> websocketMap = new ConcurrentHashMap<String, Session>();
	
	private static WebSocketManager instance;
	
	private WebSocketManager(){};
	
	public static WebSocketManager getInstance(){
		if(instance == null){
			instance = new WebSocketManager();
		}
		return instance;
	}
	
	public void add(String key, Session session) throws Exception{
		if(this.websocketMap.containsKey(key)){
			this.websocketMap.remove(key);
		}
		this.websocketMap.put(key, session);
	}
	
	public void remove(String key) throws Exception{
		this.websocketMap.remove(key);
	}
	
	public Session getSession(String key) throws Exception{
		return this.websocketMap.get(key);
	}
	
	public void remove(Session session){
		Set<String> keySet = this.websocketMap.keySet();
		for(String key: keySet){
			if(this.websocketMap.get(key).equals(session)){
				this.websocketMap.remove(key);
			}
		}
	}
	
	public ConcurrentHashMap<String, Session> getMap(){
		return websocketMap;
	}
	
}
