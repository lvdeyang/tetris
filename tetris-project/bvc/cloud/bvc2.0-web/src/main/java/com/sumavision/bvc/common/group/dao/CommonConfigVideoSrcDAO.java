package com.sumavision.bvc.common.group.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.common.group.po.CommonConfigVideoSrcPO;
import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoSrcDTO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSrcPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommonConfigVideoSrcPO.class, idClass = long.class)
public interface CommonConfigVideoSrcDAO extends MetBaseDAO<CommonConfigVideoSrcPO>{
	
//	/**
//	 * @Title: 获取视频配置中的源 
//	 * @param videoIds 视频id列表
//	 * @return List<DeviceGroupConfigVideoSrcDTO> 源
//	 */
//	@Query("select new com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoSrcDTO(src.id, src.uuid, src.updateTime, src.type, src.roleId, src.roleName, src.roleChannelType, position.serialnum, position.pictureType, position.pollingTime, position.pollingStatus, src.visible, src.memberId, src.memberChannelId, src.memberChannelName, src.layerId, src.channelId, src.channelName, src.bundleId, src.bundleName, src.virtualUuid, src.virtualName, video.id) from DeviceGroupConfigVideoSrcPO src left join src.position position left join position.video video where video.id in ?1")
//	public List<DeviceGroupConfigVideoSrcDTO> findByVideoIds(Collection<Long> videoIds);
	
	/**
	 * @Title: 根据channelId获取配置视频源 <br/>
	 * @param channelIds 通道列表
	 * @return List<CommonConfigVideoSrcPO> 视频源
	 */
	public List<CommonConfigVideoSrcPO> findByMemberChannelIdIn(Collection<Long> channelIds);
	
	@Modifying
	@Transactional
	public void deleteByRoleId(Long roleId);
	
	@Modifying
	@Transactional
	public void deleteByRoleIdIn(Collection<Long> roleIds);
	
}
