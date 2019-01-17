package com.sumavision.tetris.mims.app.organization;

import java.util.List;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = OrganizationPO.class, idClass = Long.class)
public interface OrganizationDAO extends BaseDAO<OrganizationPO>{

	/**
	 * 查询组下的组织机构列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月21日 下午3:00:30
	 * @param String groupId 用户组id
	 * @return List<OrganizationPO> 角色列表
	 */
	public List<OrganizationPO> findByGroupIdOrderBySerialAsc(String groupId);
	
}
