package com.sumavision.tetris.cms.relation;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.cms.article.ArticlePO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ColumnRelationArticlePO.class, idClass = Long.class)
public interface ColumnRelationArticleDAO extends BaseDAO<ColumnRelationArticlePO>{
	
	/**
	 * 栏目查询文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午3:59:34
	 * @param Long columnId 栏目id
	 */
	public List<ColumnRelationArticlePO> findByColumnId(Long columnId);
	
	/**
	 * 分页查询栏目文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午3:59:34
	 * @param Long columnId 栏目id
	 * @param Pageable page
	 */
	public Page<ColumnRelationArticlePO> findByColumnId(Long columnId, Pageable page);
	
	/**
	 * 栏目查询文章id<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午3:59:34
	 * @param Long columnId 栏目id
	 */
	@Query(value = "SELECT articleId FROM TETRIS_CMS_COLUMN_RELATION_ARTICLE WHERE columnId = ?1")
	public List<Long> findArticleIdByColumnId(Long columnId);
	
	/**
	 * 栏目查询文章(排序--由大到小)<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月27日 下午3:59:34
	 * @param Long columnId 栏目id
	 */
	public List<ColumnRelationArticlePO> findByColumnIdOrderByArticleOrderDesc(Long columnId);
	
	/**
	 * 文章查询栏目(排序--由大到小)<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月27日 下午3:59:34
	 * @param Long articleId 文章id
	 */
	public List<ColumnRelationArticlePO> findByArticleIdOrderByArticleOrderDesc(Long articleId);
	
	/**
	 * 栏目查询文章(排序--由小到大)<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月27日 下午3:59:34
	 * @param Long columnId 栏目id
	 */
	public List<ColumnRelationArticlePO> findByColumnIdOrderByArticleOrder(Long columnId);
	
	/**
	 * 文章查询栏目<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月27日 下午3:59:34
	 * @param Long articleId 文章id
	 */
	public List<ColumnRelationArticlePO> findByArticleId(Long articleId);
	
	/**
	 * 根据文章id列表获取关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 上午11:04:04
	 * @param ids 文章id列表
	 */
	public List<ColumnRelationArticlePO> findByArticleIdIn(Collection<Long> ids);
	
	/**
	 * 根据文章id列表删除关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 上午11:04:04
	 * @param ids 文章id列表
	 */
	public void deleteByArticleIdIn(Collection<Long> ids);
	
	/** 
	 * 根据栏目id和文章id列表查询<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午3:53:36
	 * @param columnId 栏目名称
	 * @param ids 文章id列表
	 */
	@Query(value = "SELECT relation.articleId FROM com.sumavision.tetris.cms.relation.ColumnRelationArticlePO relation WHERE relation.columnId = ?1 AND relation.articleId IN ?2")
	public Page<Long> findArticleIdByColumnAndArticle(Long columnId, List<Long> ids, Pageable page); 
	
	/**
	 * 根据推荐查询<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午3:55:40
	 * @param flag 是否推荐
	 * @return
	 */
	public List<ColumnRelationArticlePO> findByCommand(Boolean flag);
	
	/**
	 * 根据推荐分页查询<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午3:55:40
	 * @param flag 是否推荐
	 * @param page
	 */
	public Page<ColumnRelationArticlePO> findByCommand(Boolean flag, Pageable page);
	
	/**
	 * 根据组织id分页查询推荐<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 上午11:54:46
	 * @param flag 是否推荐
	 * @param groupId 组织id
	 * @param page 分页
	 * @return Page<ColumnRelationArticlePO> 关联列表
	 */
	@Query(value = "SELECT relation.* FROM tetris_cms_column_relation_article relation " +
				   "LEFT JOIN tetris_cms_article article ON relation.article_id = article.id LEFT JOIN tetris_cms_article_user_permission user1 ON article.id = user1.article_id " + 
				   "LEFT JOIN tetris_cms_column col ON relation.column_id = col.id LEFT JOIN tetris_cms_column_user_permission user2 ON col.id = user2.column_id " +
				   "WHERE relation.command = ?1 AND user1.group_id = ?2 AND user2.group_id = ?2 \n#pageable\n",
				   countQuery = "SELECT count(relation.id) FROM tetris_cms_column_relation_article relation " +
						   "LEFT JOIN tetris_cms_article article ON relation.article_id = article.id LEFT JOIN tetris_cms_article_user_permission user1 ON article.id = user1.article_id " + 
						   "LEFT JOIN tetris_cms_column col ON relation.column_id = col.id LEFT JOIN tetris_cms_column_user_permission user2 ON col.id = user2.column_id " +
						   "WHERE relation.command = ?1 AND user1.group_id = ?2 AND user2.group_id = ?2",
				   	nativeQuery = true)
	public Page<ColumnRelationArticlePO> findCommandByGroupId(Boolean flag, String groupId, Pageable page);
	
	/**
	 * 根据用户id分页查询推荐<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 上午11:56:11
	 * @param flag 是否推荐
	 * @param userId 用户id
	 * @param page 分页
	 * @return Page<ColumnRelationArticlePO> 关联列表
	 */
	@Query(value = "SELECT relation.* FROM tetris_cms_column_relation_article relation " +
			   "LEFT JOIN tetris_cms_article article ON relation.article_id = article.id LEFT JOIN tetris_cms_article_user_permission user1 ON article.id = user1.article_id " + 
			   "LEFT JOIN tetris_cms_column col ON relation.column_id = col.id LEFT JOIN tetris_cms_column_user_permission user2 ON col.id = user2.column_id " +
			   "WHERE relation.command = ?1 AND user1.user_id = ?2 AND user2.user_id = ?2 \n#pageable\n",
			   countQuery = "SELECT count(relation.id) FROM tetris_cms_column_relation_article relation " +
					   "LEFT JOIN tetris_cms_article article ON relation.article_id = article.id LEFT JOIN tetris_cms_article_user_permission user1 ON article.id = user1.article_id " + 
					   "LEFT JOIN tetris_cms_column col ON relation.column_id = col.id LEFT JOIN tetris_cms_column_user_permission user2 ON col.id = user2.column_id " +
					   "WHERE relation.command = ?1 AND user1.user_id = ?2 AND user2.user_id = ?2",
			   	nativeQuery = true)
	public Page<ColumnRelationArticlePO> findCommandByUserId(Boolean flag, String userId, Pageable page);
	
	/**
	 * 根据搜索查询<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午3:55:40
	 * @param reg 搜索条件
	 * @param page 分页
	 * @return Page<ColumnRelationArticlePO> 关联列表
	 */
	@Query(value = "SELECT relation FROM com.sumavision.tetris.cms.relation.ColumnRelationArticlePO relation WHERE relation.articleName LIKE ?1")
	public Page<ColumnRelationArticlePO> findAllBySearch(String reg, Pageable page);
	
	/**
	 * 根据组织id搜索查询<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 下午1:21:38
	 * @param reg 搜索条件
	 * @param groupId 组织id
	 * @param pageable 分页
	 * @return Page<ColumnRelationArticlePO> 关联列表
	 */
	@Query(value = "SELECT relation.* FROM tetris_cms_column_relation_article relation " +
			   "LEFT JOIN tetris_cms_article article ON relation.article_id = article.id LEFT JOIN tetris_cms_article_user_permission user1 ON article.id = user1.article_id " + 
			   "LEFT JOIN tetris_cms_column col ON relation.column_id = col.id LEFT JOIN tetris_cms_column_user_permission user2 ON col.id = user2.column_id " +
			   "WHERE relation.article_name LIKE ?1 AND user1.group_id = ?2 AND user2.group_id = ?2 \n#pageable\n",
			   countQuery = "SELECT count(relation.id) FROM tetris_cms_column_relation_article relation " +
					   "LEFT JOIN tetris_cms_article article ON relation.article_id = article.id LEFT JOIN tetris_cms_article_user_permission user1 ON article.id = user1.article_id " + 
					   "LEFT JOIN tetris_cms_column col ON relation.column_id = col.id LEFT JOIN tetris_cms_column_user_permission user2 ON col.id = user2.column_id " +
					   "WHERE relation.article_name LIKE ?1 AND user1.group_id = ?2 AND user2.group_id = ?2",
			   	nativeQuery = true)
	public Page<ColumnRelationArticlePO> findAllWithGroupIdBySearch(String reg, String groupId, Pageable pageable);
	
	/**
	 * 根据用户id搜索查询<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月28日 下午1:23:33
	 * @param reg 搜索条件
	 * @param userId 用户id
	 * @param pageable 分页
	 * @return Page<ColumnRelationArticlePO> 关联列表
	 */
	@Query(value = "SELECT relation.* FROM tetris_cms_column_relation_article relation " +
			   "LEFT JOIN tetris_cms_article article ON relation.article_id = article.id LEFT JOIN tetris_cms_article_user_permission user1 ON article.id = user1.article_id " + 
			   "LEFT JOIN tetris_cms_column col ON relation.column_id = col.id LEFT JOIN tetris_cms_column_user_permission user2 ON col.id = user2.column_id " +
			   "WHERE relation.article_name LIKE ?1 AND user1.user_id = ?2 AND user2.user_id = ?2 \n#pageable\n",
			   countQuery = "SELECT count(relation.id) FROM tetris_cms_column_relation_article relation " +
					   "LEFT JOIN tetris_cms_article article ON relation.article_id = article.id LEFT JOIN tetris_cms_article_user_permission user1 ON article.id = user1.article_id " + 
					   "LEFT JOIN tetris_cms_column col ON relation.column_id = col.id LEFT JOIN tetris_cms_column_user_permission user2 ON col.id = user2.column_id " +
					   "WHERE relation.article_name LIKE ?1 AND user1.user_id = ?2 AND user2.user_id = ?2",
			   	nativeQuery = true)
	public Page<ColumnRelationArticlePO> findAllWithUserIdBySearch(String reg, String userId, Pageable pageable);
	
	/**
	 * 根据栏目编号列表查询关联表<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月13日 下午5:31:29
	 * @param codes 栏目编号列表
	 * @return List<ColumnRelationArticlePO> 关联表
	 */
	public List<ColumnRelationArticlePO> findByColumnCodeIn(List<String> codes);

}
