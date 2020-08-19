package com.sumavision.tetris.easy.process.access.point;

import java.util.List;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AccessPointProcessPermissionPO.class, idClass = Long.class)
public interface AccessPointProcessPermissionDAO extends BaseDAO<AccessPointProcessPermissionPO>{

	/**
	 * 查询流程下的接入点引用<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月28日 下午3:54:31
	 * @param Long processId 流程id
	 * @return List<AccessPointProcessPermissionPO> 引用列表
	 */
	public List<AccessPointProcessPermissionPO> findByProcessId(Long processId);
	
	/**
	 * 查询接入点的所有引用<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月28日 下午4:20:31
	 * @param Long accessPointId 接入点id
	 * @return List<AccessPointProcessPermissionPO> 引用列表
	 */
	public List<AccessPointProcessPermissionPO> findByAccessPointId(Long accessPointId);
	
}
