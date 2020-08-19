package com.sumavision.bvc.device.group.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.dto.DeviceGroupDTO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupPO.class, idClass = long.class)
public interface DeviceGroupDAO extends MetBaseDAO<DeviceGroupPO>{

	//@Query("select new com.sumavision.bvc.device.group.dto.DeviceGroupDTO(g.id, g.uuid, g.updateTime, g.name, g.userId, g.userName, g.transmissionMode, g.type, t.avTplId, t.name, g.systemTplId, g.systemTplName, g.dicRegionId, g.dicRegionContent, g.dicClassifyId, g.dicClassifyContent) from DeviceGroupPO g left join g.avtpl t on g.avtpl.id=t.id where (g.type='MEETING' or g.type='MONITOR') and g.userId=?1")
	@Query("select new com.sumavision.bvc.device.group.dto.DeviceGroupDTO(g.id, g.uuid, g.updateTime, g.name, g.userId, g.userName, g.transmissionMode, g.forwardMode, g.type, g.status, t.avTplId, t.name, g.systemTplId, g.systemTplName, g.dicRegionId, g.dicRegionContent, g.dicProgramId, g.dicProgramContent, g.dicCategoryLiveId, g.dicCategoryLiveContent, g.dicStorageLocationCode, g.dicStorageLocationContent) from DeviceGroupPO g left join g.avtpl t on g.avtpl.id=t.id where (g.type='MEETING' or g.type='MONITOR') and g.userId=?1")
	public Page<DeviceGroupDTO> findAllByUserIdOutputDTO(Long userId, Pageable pageable);

	@Query("select new com.sumavision.bvc.device.group.dto.DeviceGroupDTO(g.id, g.uuid, g.updateTime, g.name, g.userId, g.userName, g.transmissionMode, g.forwardMode, g.type, g.status, t.avTplId, t.name, g.systemTplId, g.systemTplName, g.dicRegionId, g.dicRegionContent, g.dicProgramId, g.dicProgramContent, g.dicCategoryLiveId, g.dicCategoryLiveContent, g.dicStorageLocationCode, g.dicStorageLocationContent) from DeviceGroupPO g left join g.avtpl t on g.avtpl.id=t.id where g.type='MEETING' or g.type='MONITOR'")
	public Page<DeviceGroupDTO> findAllOutputDTO(Pageable pageable);
	
	@Query("select new com.sumavision.bvc.device.group.dto.DeviceGroupDTO(g.id, g.uuid, g.updateTime, g.name, g.userId, g.userName, g.transmissionMode, g.forwardMode, g.type, g.status, t.id, t.name, g.systemTplId, g.systemTplName, g.dicRegionId, g.dicRegionContent, g.dicProgramId, g.dicProgramContent, g.dicCategoryLiveId, g.dicCategoryLiveContent, g.dicStorageLocationCode, g.dicStorageLocationContent) from DeviceGroupPO g left join g.avtpl t on g.avtpl.id=t.id")
	public DeviceGroupPO findAllOutputDTO(Long groupId);
	
	public List<DeviceGroupPO> findByUserId(Long userId);
	
	@Query("select group.volume from DeviceGroupPO group where group.id=?1")
	public int findGroupVolumeByGroupId(Long groupId);	

	public DeviceGroupPO findByName(String name);

	/** 设备呼叫会议，演示使用 */
	public List<DeviceGroupPO> findByNameLike(String name);
}
