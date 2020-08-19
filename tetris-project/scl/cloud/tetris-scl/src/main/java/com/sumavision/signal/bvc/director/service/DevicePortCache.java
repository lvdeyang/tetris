package com.sumavision.signal.bvc.director.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.sumavision.signal.bvc.director.dao.DirectorSourceDAO;
import com.sumavision.signal.bvc.director.po.DirectorSourcePO;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 设备在能力端口占用缓存<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月12日 上午9:29:14
 */
public class DevicePortCache {

	/** key:bundleId; value:capacityIp@capacityPort */
	private ConcurrentHashMap<String, String> portMap;
	
	private DevicePortCache() {
		this.portMap = new ConcurrentHashMap<String, String>();
	}
	
	private static DevicePortCache instance;
	
	public static DevicePortCache getInstance(){
		synchronized (DevicePortCache.class) {
			if(instance == null){
				instance = new DevicePortCache();
			}
			return instance;
		}
	}
	
	public ConcurrentHashMap<String, String> getMap(){
		return this.portMap;
	}
	
	public String getByKey(String key){
		return portMap.get(key);
	}
	
	public synchronized void add(String key, String value){
		portMap.put(key, value);
	}
	
	public synchronized void remove(String key){
		if(portMap.containsKey(key)){
			portMap.remove(key);
		}
	}
	
	/**
	 * 初始化设备端口缓存<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月12日 下午1:41:41
	 */
	public void initCache() throws Exception{
		
		DirectorSourceDAO directorSourceDao = SpringContext.getBean(DirectorSourceDAO.class);
		
		List<DirectorSourcePO> sources = directorSourceDao.findAll();
		
		for(DirectorSourcePO source: sources){
			if(source.getBundleId() != null && source.getCapacityPort() != null){
				add(source.getBundleId(), new StringBufferWrapper().append(source.getCapacityIp())
																   .append("@")
																   .append(source.getCapacityPort())
																   .toString());
			}
		}
	}
	
}
