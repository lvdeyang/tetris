package com.sumavision.bvc.device.group.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoSmallScreenSrcDTO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSmallSrcPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupConfigVideoSmallSrcPO.class, idClass = long.class)
public interface DeviceGroupConfigVideoSmallSrcDAO extends MetBaseDAO<DeviceGroupConfigVideoSmallSrcPO>{

	/**
	 * @Title: 获取视频配置中小屏的源 
	 * @param videoIds 视频id列表
	 * @return List<DeviceGroupConfigVideoSmallScreenSrcDTO> 源
	 */
	@Query("select new com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoSmallScreenSrcDTO(src.id, src.uuid, src.updateTime, src.type, src.roleId, src.roleName, src.roleChannelType, src.memberId, src.memberChannelId, src.memberChannelName, src.layerId, src.channelId, src.channelName, src.bundleId, src.bundleName, src.virtualUuid, src.virtualName, video.id) from DeviceGroupConfigVideoSmallSrcPO src left join src.video video where video.id in ?1")
	public List<DeviceGroupConfigVideoSmallScreenSrcDTO> findByVideoIds(Collection<Long> videoIds);
}
