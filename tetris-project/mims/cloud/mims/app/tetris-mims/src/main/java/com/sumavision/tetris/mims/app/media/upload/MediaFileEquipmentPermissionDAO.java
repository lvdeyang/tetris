package com.sumavision.tetris.mims.app.media.upload;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = MediaFileEquipmentPermissionPO.class, idClass = Long.class)
public interface MediaFileEquipmentPermissionDAO extends BaseDAO<MediaFileEquipmentPermissionPO>{
	public List<MediaFileEquipmentPermissionPO> findByMediaIdAndMediaType(Long mediaId, String mediaType);
	
	public List<MediaFileEquipmentPermissionPO> findByMediaIdInAndMediaType(List<Long> mediaId, String mediaType);
	
	public MediaFileEquipmentPermissionPO findByMediaIdAndMediaTypeAndEquipmentIp(Long mediaId, String mediaType, String equipmentIp);
	
	public List<MediaFileEquipmentPermissionPO> findByMediaIdInAndMediaTypeAndEquipmentIp(List<Long> mediaId, String mediaType, String equipmentIp);
}
