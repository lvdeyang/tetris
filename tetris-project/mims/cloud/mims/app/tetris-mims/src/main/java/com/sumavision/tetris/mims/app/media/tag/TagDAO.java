package com.sumavision.tetris.mims.app.media.tag;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TagPO.class, idClass = Long.class)
public interface TagDAO extends BaseDAO<TagPO>{

	@Query(value = "SELECT tag.* FROM TETRIS_TAG tag "
			+ "LEFT JOIN TETRIS_TAG_GROUP_PERMISSION permission "
			+ "ON tag.id = permission.tag_id "
			+ "WHERE tag.id = ?1 "
			+ "AND permission.group_id = ?2", nativeQuery = true)
	public TagPO findByIdAndGroupId(Long id, String groupId);
	
	@Query(value = "SELECT DISTINCT tag.* FROM TETRIS_TAG tag "
			+ "LEFT JOIN TETRIS_TAG_GROUP_PERMISSION permission "
			+ "ON tag.id = permission.tag_id "
			+ "WHERE tag.parent_id = ?1 "
			+ "AND permission.group_id = ?2", nativeQuery = true)
	public List<TagPO> findByParentIdAndGroupId(Long parentId, String groupId);
	
	@Query(value = "SELECT tag.id FROM TETRIS_TAG tag "
			+ "LEFT JOIN TETRIS_TAG_GROUP_PERMISSION permission "
			+ "ON tag.id = permission.tag_id "
			+ "WHERE tag.parent_id = ?1 "
			+ "AND permission.group_id = ?2", nativeQuery = true)
	public List<Long> findIdsByParentIdAndGroupId(Long parentId, String groupId);
	
	@Query(value = "SELECT DISTINCT tag.* FROM TETRIS_TAG tag "
			+ "LEFT JOIN TETRIS_TAG_GROUP_PERMISSION permission "
			+ "ON tag.id = permission.tag_id "
			+ "WHERE tag.parent_id is null "
			+ "AND permission.group_id = ?1", nativeQuery = true)
	public List<TagPO> findRootByGroupId(String groupId);
	
	@Query(value = "SELECT tag.* FROM TETRIS_TAG tag "
			+ "LEFT JOIN TETRIS_TAG_GROUP_PERMISSION permission "
			+ "ON tag.id = permission.tag_id "
			+ "WHERE permission.group_id = ?1 "
			+ "AND tag.name = ?2", nativeQuery = true)
	public TagPO findByName(String groupId, String name);
	
	@Query(value = "SELECT tag.* FROM TETRIS_TAG tag "
			+ "LEFT JOIN TETRIS_TAG_GROUP_PERMISSION permission "
			+ "ON tag.id = permission.tag_id "
			+ "WHERE permission.group_id = ?1 "
			+ "AND tag.name in ?2", nativeQuery = true)
	public List<TagPO> findByNameIn(String groupId, List<String> names);
	
	@Query(value = "SELECT tag.* FROM TETRIS_TAG tag "
			+ "LEFT JOIN TETRIS_TAG_GROUP_PERMISSION permission "
			+ "ON tag.id = permission.tag_id "
			+ "WHERE tag.id <> ?1 "
			+ "AND permission.group_id = ?2 "
			+ "AND tag.name = ?3", nativeQuery = true)
	public TagPO findByNameAndId(Long id, String groupId, String name);
}
