package com.sumavision.tetris.cms.classify;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ClassifyPO.class, idClass = Long.class)
public interface ClassifyDAO extends BaseDAO<ClassifyPO>{

	/**
	 * 分页查询分类列表（带例外）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月2日 下午6:29:45
	 * @param Collection<Long> except 例外分类id列表
	 * @param Pageable page 分页信息
	 * @return Page<ClassifyPO> 分类列表
	 */
	@Query(value = "SELECT classify FROM com.sumavision.tetris.cms.classify.ClassifyPO classify WHERE classify.id NOT IN ?1")
	public Page<ClassifyPO> findWithExcept(Collection<Long> except, Pageable page);
	
	/**
	 * 根据用户id分页查询分类列表（带例外）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 上午10:27:38
	 * @param except 例外分类id列表
	 * @param userId 用户id
	 * @param page 分页
	 * @return Page<ClassifyPO> 分类列表
	 */
	@Query(value = "SELECT classify.* FROM TETRIS_CMS_CLASSIFY classify LEFT JOIN FROM TETRIS_CMS_CLASSIFY_USER_PERMISSION permission ON classify.id = permission.classify_id WHERE classify.id NOT IN ?1 AND permission.user_id = ?2 \n#pageable\n",
			countQuery = "SELECT count(classify.id) FROM TETRIS_CMS_CLASSIFY classify LEFT JOIN FROM TETRIS_CMS_CLASSIFY_USER_PERMISSION permission ON classify.id = permission.classify_id WHERE classify.id NOT IN ?1 AND permission.user_id = ?2",
			nativeQuery = true)
	public Page<ClassifyPO> findWithExceptByUserId(Collection<Long> except, String userId, Pageable page);
	
	/**
	 * 根据组织id分页查询分类列表（带例外）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 上午10:27:42
	 * @param except 例外分类id列表
	 * @param groupId 组织id
	 * @param page 分页
	 * @return Page<ClassifyPO> 分类列表
	 */
	@Query(value = "SELECT classify.* FROM TETRIS_CMS_CLASSIFY classify LEFT JOIN FROM TETRIS_CMS_CLASSIFY_USER_PERMISSION permission ON classify.id = permission.classify_id WHERE classify.id NOT IN ?1 AND permission.group_id = ?2 \n#pageable\n",
			countQuery = "SELECT count(classify.id) FROM TETRIS_CMS_CLASSIFY classify LEFT JOIN FROM TETRIS_CMS_CLASSIFY_USER_PERMISSION permission ON classify.id = permission.classify_id WHERE classify.id NOT IN ?1 AND permission.group_id = ?2",
			nativeQuery = true)
	public Page<ClassifyPO> findWithExceptByGroupId(Collection<Long> except, String groupId, Pageable page);
	
	
	/**
	 * 根据用户id分页查询分类列表<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 上午8:59:36
	 * @param userId 用户id
	 * @param page 分页
	 * @return Page<ClassifyPO> 分类列表
	 */
	@Query(value = "SELECT classify.* FROM TETRIS_CMS_CLASSIFY classify LEFT JOIN TETRIS_CMS_CLASSIFY_USER_PERMISSION permission ON classify.id = permission.classify_id WHERE permission.user_id = ?1 \n#pageable\n",
			countQuery = "SELECT count(classify.id) FROM TETRIS_CMS_CLASSIFY classify LEFT JOIN TETRIS_CMS_CLASSIFY_USER_PERMISSION permission ON classify.id = permission.classify_id WHERE permission.user_id = ?1",
			nativeQuery = true)
	public Page<ClassifyPO> findAllByUserId(String userId, Pageable page);
	
	/**
	 * 根据组织id分页查询分类列表<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 上午9:01:45
	 * @param groupId 组织id
	 * @param page 分页
	 * @return Page<ClassifyPO> 分类列表
	 */
	@Query(value = "SELECT classify.* FROM TETRIS_CMS_CLASSIFY classify LEFT JOIN TETRIS_CMS_CLASSIFY_USER_PERMISSION permission ON classify.id = permission.classify_id WHERE permission.group_id = ?1 \n#pageable\n",
			countQuery = "SELECT count(classify.id) FROM TETRIS_CMS_CLASSIFY classify LEFT JOIN TETRIS_CMS_CLASSIFY_USER_PERMISSION permission ON classify.id = permission.classify_id WHERE permission.group_id = ?1",
			nativeQuery = true)
	public Page<ClassifyPO> findAllByGroupId(String groupId, Pageable page);
}
