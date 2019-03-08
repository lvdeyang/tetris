package com.sumavision.tetris.mvc.ext.context;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;

/**
 * http session上下文缓存<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月6日 上午10:39:26
 */
public class HttpSessionContext {

	private static final ConcurrentHashMap<String, HttpSession> context = new ConcurrentHashMap<String, HttpSession>();
	
	/**
	 * 缓存一个session<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 上午10:41:52
	 * @param String id sessionId
	 * @param HttpSession session 
	 */
	public static void put(String id, HttpSession session){
		context.put(id, session);
	}
	
	/**
	 * 获取一个session<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 上午10:42:36
	 * @param String id sessionId
	 * @return HttpSession session
	 */
	public static HttpSession get(String id){
		if(id == null){
			return null;
		}
		return context.get(id);
	}
	
	/**
	 * 移除一个session<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 上午10:43:16
	 * @param String id sessionId
	 */
	public static void remove(HttpSession session){
		Set<String> keys = new HashSet<String>();
		Set<Entry<String, HttpSession>> entries = context.entrySet();
		for(Entry<String, HttpSession> entry:entries){
			if(entry.getValue().equals(session)){
				keys.add(entry.getKey());
			}
		}
		if(keys.size() > 0){
			for(String key:keys){
				context.remove(key);
			}
		}
	}
	
}
