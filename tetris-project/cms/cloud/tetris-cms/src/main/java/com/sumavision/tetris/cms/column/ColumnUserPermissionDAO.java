package com.sumavision.tetris.cms.column;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ColumnUserPermissionPO.class, idClass = Long.class)
public interface ColumnUserPermissionDAO extends BaseDAO<ColumnUserPermissionPO>{

	/**
	 * 根据栏目id删除栏目用户关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 下午1:53:54
	 * @param columnId 栏目id
	 */
	public void deleteByColumnId(Long columnId);
	
	/**
	 * 根据栏目和用户查询关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 下午3:42:17
	 * @param columnId 栏目id
	 * @param groupId 组织id
	 * @return ColumnUserPermissionPO 关联
	 */
	public ColumnUserPermissionPO findByColumnIdAndGroupId(Long columnId, String groupId);
	
	/**
	 * 根据栏目和用户查询关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 下午3:42:17
	 * @param columnId 栏目id
	 * @param userId 用户id
	 * @return ColumnUserPermissionPO 关联
	 */
	public ColumnUserPermissionPO findByColumnIdAndUserId(Long columnId, String userId);
}
