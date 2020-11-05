package com.sumavision.bvc.device.group.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.bvc.device.group.po.DeviceGroupScreenLayoutPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;


@RepositoryDefinition(domainClass = DeviceGroupScreenLayoutPO.class, idClass = long.class)
public interface DeviceGroupScreenLayoutDAO extends MetBaseDAO<DeviceGroupScreenLayoutPO>{

	/**
	 * @Title: 查询设备组中的布局方案 
	 * @param groupId 设备组id
	 * @return List<DeviceGroupScreenLayoutPO> 布局方案
	 * @throws
	 */
	@Query("select layout from DeviceGroupScreenLayoutPO layout where layout.group.id=?1")
	public List<DeviceGroupScreenLayoutPO> findByGroupId(Long groupId);
	
}
