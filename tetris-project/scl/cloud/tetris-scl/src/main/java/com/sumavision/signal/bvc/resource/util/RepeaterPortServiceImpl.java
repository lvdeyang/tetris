package com.sumavision.signal.bvc.resource.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sun.jersey.client.impl.CopyOnWriteHashMap;

/**
 * 转发器网口端口链路维护<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月22日 下午4:50:04
 */
public class RepeaterPortServiceImpl {
	
	private ConcurrentHashMap<String, CopyOnWriteArrayList<Long>> mapping = null;
	
	private static RepeaterPortServiceImpl instance;
	
	private RepeaterPortServiceImpl(){}
	
	public static RepeaterPortServiceImpl getInstance(){
		if(instance == null){
			instance = new RepeaterPortServiceImpl();
		}
		return instance;
	}
	
}
