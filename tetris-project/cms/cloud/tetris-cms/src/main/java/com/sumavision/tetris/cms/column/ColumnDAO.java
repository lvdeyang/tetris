package com.sumavision.tetris.cms.column;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ColumnPO.class, idClass = Long.class)
public interface ColumnDAO extends BaseDAO<ColumnPO>{

	
	@Query(value = "SELECT * FROM TETRIS_CMS_COLUMN WHERE PARENT_PATH LIKE ?1 OR PARENT_PATH LIKE ?2", nativeQuery = true)
	public List<ColumnPO> findAllSubColumns(String reg1, String reg2);
	
	/**
	 * 根据栏目编码列表查询栏目列表<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月13日 下午5:09:24
	 * @param codes 栏目编码列表
	 * @return List<ColumnPO>
	 */
	public List<ColumnPO> findByCodeIn(List<String> codes);
	
	/**
	 * 根据组织id查询栏目<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 上午11:41:30
	 * @param groupId 组织id
	 * @return List<ColumnPO> 栏目列表
	 */
	@Query(value = "SELECT col.* FROM TETRIS_CMS_COLUMN col LEFT JOIN TETRIS_CMS_COLUMN_USER_PERMISSION permission ON col.id = permission.column_id WHERE permission.group_id = ?1", nativeQuery = true)
	public List<ColumnPO> findByGroupId(String groupId);
	
	/**
	 * 根据用户id查询栏目<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 上午11:41:30
	 * @param userId 用户id
	 * @return List<ColumnPO> 栏目列表
	 */
	@Query(value = "SELECT col.* FROM TETRIS_CMS_COLUMN col LEFT JOIN TETRIS_CMS_COLUMN_USER_PERMISSION permission ON col.id = permission.column_id WHERE permission.user_id = ?1", nativeQuery = true)
	public List<ColumnPO> findByUserId(String userId);
}
