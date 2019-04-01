package com.sumavision.tetris.cms.classify;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ClassifyUserPermissionPO.class, idClass = Long.class)
public interface ClassifyUserPermissionDAO extends BaseDAO<ClassifyUserPermissionPO>{

	/**
	 * 根据分类id删除关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 上午9:56:50
	 * @param classifyId 分类id
	 */
	public void deleteByClassifyId(Long classifyId);
	
	/**
	 * 根据分类和用户查询关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 下午3:25:51
	 * @param classifyId 分类id
	 * @param groupId 组织id
	 * @return ClassifyUserPermissionPO 关联
	 */
	public ClassifyUserPermissionPO findByClassifyIdAndGroupId(Long classifyId, String groupId);
	
	/**
	 * 根据分类和组织查询关联<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 下午3:26:36
	 * @param classifyId 分类id
	 * @param userId 用户id
	 * @return ClassifyUserPermissionPO 关联
	 */
	public ClassifyUserPermissionPO findByClassifyIdAndUserId(Long classifyId, String userId);
}
