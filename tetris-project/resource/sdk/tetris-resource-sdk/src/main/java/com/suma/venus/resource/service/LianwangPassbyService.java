package com.suma.venus.resource.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.LianwangPassbyDAO;
import com.suma.venus.resource.pojo.LianwangPassbyPO;

@Service
@Transactional(rollbackOn = Exception.class)
public class LianwangPassbyService {

	@Autowired
	private LianwangPassbyDAO lianwangPassbyDao;
	
	/**
	 * 保存passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月28日 下午4:08:40
	 * @param String uuid 业务uuid
	 * @param String layerId 联网layerId
	 * @param String type 业务类型
	 * @param String protocol passby
	 */
	public void save(
			String uuid, 
			String layerId, 
			String type, 
			String protocol) throws Exception{
		LianwangPassbyPO passbyEntity = new LianwangPassbyPO();
		passbyEntity.setUuid(uuid);
		passbyEntity.setLayerId(layerId);
		passbyEntity.setType(type);
		passbyEntity.setProtocol(protocol);
		lianwangPassbyDao.save(passbyEntity);
	}
	
	/**
	 * 删除passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月28日 下午4:09:44
	 * @param String uuid passby uuid
	 */
	public void delete(String uuid) throws Exception{
		LianwangPassbyPO passby = lianwangPassbyDao.findByUuid(uuid);
		if(passby != null){
			lianwangPassbyDao.delete(passby);
		}
	}
	
}
