package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.LianwangPassbyDAO;
import com.suma.venus.resource.pojo.LianwangPassbyPO;
import com.suma.venus.resource.vo.LianwangPassbyVO;

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
	 * 保存并覆盖passby<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月28日 下午4:08:40
	 * @param String uuid 业务uuid
	 * @param String layerId 联网layerId
	 * @param String type 业务类型
	 * @param String protocol passby
	 */
	public void saveAndCover(
			String uuid, 
			String layerId, 
			String type, 
			String protocol) throws Exception{
		LianwangPassbyPO passby = lianwangPassbyDao.findByUuidAndType(uuid, type);
		
		if(passby == null){
			passby = new LianwangPassbyPO();
		}
		
		passby.setUuid(uuid);
		passby.setLayerId(layerId);
		passby.setType(type);
		passby.setProtocol(protocol);
		lianwangPassbyDao.save(passby);
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
	
	/**
	 * 根据节点id查询passby<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月27日 上午10:41:03
	 * @param layerId 联网接入id
	 * @return List<LianwangPassbyVO> passby消息
	 */
	public List<LianwangPassbyVO> queryPassby(String layerId)throws Exception{
		List<LianwangPassbyPO> passbyPOs = lianwangPassbyDao.findByLayerId(layerId);
		List<LianwangPassbyVO> passbyVOs = new ArrayList<LianwangPassbyVO>();
		if (passbyPOs != null && !passbyPOs.isEmpty()) {
			for (LianwangPassbyPO lianwangPassbyPO : passbyPOs) {
				LianwangPassbyVO lianwangPassbyVO = new LianwangPassbyVO().set(lianwangPassbyPO);
				passbyVOs.add(lianwangPassbyVO);
			}
		}
		return passbyVOs;
	}
}
