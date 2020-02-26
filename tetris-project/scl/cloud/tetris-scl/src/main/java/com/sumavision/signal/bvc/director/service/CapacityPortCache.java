package com.sumavision.signal.bvc.director.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.sumavision.signal.bvc.director.dao.DirectorDstDAO;
import com.sumavision.signal.bvc.director.dao.DirectorSourceDAO;
import com.sumavision.signal.bvc.director.po.DirectorDstPO;
import com.sumavision.signal.bvc.director.po.DirectorSourcePO;
import com.sumavision.tetris.commons.context.SpringContext;

/**
 * 能力端口缓存<br/>
 * <b>作者:</b>sm<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月21日 下午3:46:35
 */
public class CapacityPortCache {
	
	private ConcurrentHashMap<String, CopyOnWriteArraySet<Long>> portMap;
	
	private CapacityPortCache() {
		this.portMap = new ConcurrentHashMap<String, CopyOnWriteArraySet<Long>>();
	}
	
	private static CapacityPortCache instance;
	
	public static CapacityPortCache getInstance(){
		synchronized (CapacityPortCache.class) {
			if(instance == null){
				instance = new CapacityPortCache();
			}
			return instance;
		}
	}
	
	public ConcurrentHashMap<String, CopyOnWriteArraySet<Long>> getMap(){
		return this.portMap;
	}
	
	public CopyOnWriteArraySet<Long> getByKey(String key){
		return portMap.get(key);
	}
	
	public synchronized void add(String key, Long value){
		CopyOnWriteArraySet<Long> portList = portMap.get(key);
		if(portList == null){
			portList = new CopyOnWriteArraySet<Long>();
			portMap.put(key, portList);
		}
		portList.add(value);
	}
	
	public synchronized void remove(String key, Long value){
		CopyOnWriteArraySet<Long> portList = portMap.get(key);
		if(portList == null) return;
		portList.remove(value);
	}
	
	public synchronized void remove(String key){
		if(portMap.containsKey(key)){
			portMap.remove(key);
		}
	}
	
	/**
	 * 初始化能力端口缓存<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月22日 下午1:41:41
	 */
	public void initCache() throws Exception{
		
		DirectorSourceDAO directorSourceDao = SpringContext.getBean(DirectorSourceDAO.class);
		
		List<DirectorSourcePO> sources = directorSourceDao.findAll();
		
		for(DirectorSourcePO source: sources){
			if(source.getCapacityIp() != null && source.getCapacityPort() != null){
				add(source.getCapacityIp(), source.getCapacityPort());
			}
		}
	}

}
