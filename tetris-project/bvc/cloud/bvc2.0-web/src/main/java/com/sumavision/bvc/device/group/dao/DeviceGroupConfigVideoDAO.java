package com.sumavision.bvc.device.group.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoDTO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupConfigVideoPO.class, idClass = long.class)
public interface DeviceGroupConfigVideoDAO extends MetBaseDAO<DeviceGroupConfigVideoPO>{
	
	/**
	 * @Title: 查询配置中的视频配置 
	 * @param configId 配置id
	 * @return List<DeviceGroupConfigVideoDTO> 视频配置
	 */
	@Query("select new com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoDTO(video.id, video.uuid, video.updateTime, video.name, video.videoOperation, video.websiteDraw, video.layout, video.record) from DeviceGroupConfigVideoPO video where video.config.id=?1")
	public List<DeviceGroupConfigVideoDTO> findByConfigIdOutPutDTO(Long configId);
	
}
