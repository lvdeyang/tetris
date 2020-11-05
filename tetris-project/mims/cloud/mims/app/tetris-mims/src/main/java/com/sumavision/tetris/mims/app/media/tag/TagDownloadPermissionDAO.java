package com.sumavision.tetris.mims.app.media.tag;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TagDownloadPermissionPO.class, idClass = Long.class)
public interface TagDownloadPermissionDAO extends BaseDAO<TagDownloadPermissionPO>{
	
	/**
	 * 根据用户id和标签id查询<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月10日 下午4:34:35
	 * @param userId 用户id
	 * @param tagId 标签id
	 * @return TagDownloadPermissionPO 关联表数据
	 */
	public TagDownloadPermissionPO findByUserIdAndTagId(Long userId, Long tagId);
	
	public List<TagDownloadPermissionPO> findByUserIdOrderByDownloadCountDesc(Long userId);
	
	@Query(value = "SELECT * FROM TETRIS_TAG_USER_DOWNLOAD_PERMISSION WHERE user_id = ?1 AND tag_id <> ?2 ORDER BY DOWNLOAD_COUNT DESC", nativeQuery = true)
	public List<TagDownloadPermissionPO> findByUserIdWithExceptTagIdOrderByDownloadCountDesc(Long userId, Long tagId); 
	
	public List<TagDownloadPermissionPO> findByTagIdOrderByDownloadCountDesc(Long tagId);
}
