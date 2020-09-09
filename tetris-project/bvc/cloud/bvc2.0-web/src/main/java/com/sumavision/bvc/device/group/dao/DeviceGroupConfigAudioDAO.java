package com.sumavision.bvc.device.group.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigAudioPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupConfigAudioPO.class, idClass = long.class)
public interface DeviceGroupConfigAudioDAO extends MetBaseDAO<DeviceGroupConfigAudioPO>{
	
	/**
	 * @Title: 获取配置中所有的音频<br/> 
	 * @param configId 配置id
	 * @return List<DeviceGroupConfigAudioPO>
	 */
	public List<DeviceGroupConfigAudioPO> findByConfigId(Long configId);
	
	/**
	 * @Title: 根据channelId获取配置音频 <br/>
	 * @param channelIds 通道列表
	 * @return List<DeviceGroupConfigAudioPO> 音频
	 */
	public List<DeviceGroupConfigAudioPO> findByMemberChannelIdIn(Collection<Long> channelIds);
	
}
