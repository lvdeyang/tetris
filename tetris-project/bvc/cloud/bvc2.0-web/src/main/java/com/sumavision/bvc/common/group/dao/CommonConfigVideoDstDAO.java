package com.sumavision.bvc.common.group.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.common.group.po.CommonConfigVideoDstPO;
import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoDstDTO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoDstPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommonConfigVideoDstPO.class, idClass = long.class)
public interface CommonConfigVideoDstDAO extends MetBaseDAO<CommonConfigVideoDstPO>{
	
	/**
//	* @Title: 根据视频ids找到所有转发目的源 
//	* @param @param videoId 视频id
//	* @param @return    设定文件 
//	* @return List<DeviceGroupConfigVideoDstDTO> 源List
//	 */
//	@Query("select new com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoDstDTO(dst.id, dst.uuid, dst.updateTime, dst.type, dst.roleId, dst.roleName, dst.roleChannelType, dst.memberId, dst.memberChannelId, dst.layerId, dst.channelId, dst.channelName, dst.bundleId, dst.bundleName, dst.bundleType, dst.screenId, dst.memberScreenId, dst.memberScreenName, dst.video.id) from DeviceGroupConfigVideoDstPO dst where dst.video.id in ?1")
//	public List<DeviceGroupConfigVideoDstDTO> findByVideoIds(Collection<Long> videoIds);

	/**
	 * @Title: 根据channelId获取配置视频目的 <br/>
	 * @param channelIds 通道列表
	 * @return List<CommonConfigVideoDstPO> 视频目的
	 */
	public List<CommonConfigVideoDstPO> findByMemberChannelIdIn(Collection<Long> channelIds);

	public List<CommonConfigVideoDstPO> findByMemberIdIn(Collection<Long> memberIds);
	
	@Modifying
	@Transactional
	public void deleteByRoleId(Long roleId);
	
	@Modifying
	@Transactional
	public void deleteByRoleIdIn(Collection<Long> roleIds);
	
}
