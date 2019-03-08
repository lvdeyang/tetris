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
	 * 栏目查询文章(排序--由小到大)<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月27日 下午3:59:34
	 * @param Long columnId 栏目id
	 */
	public List<ColumnRelationArticlePO> findByColumnIdOrderByArticleOrder(Long columnId);
	
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
	 * 根据推荐查询<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午3:55:40
	 * @param flag 是否推荐
	 * @return
	 */
	@Query(value = "SELECT relation FROM com.sumavision.tetris.cms.relation.ColumnRelationArticlePO relation WHERE relation.articleName LIKE ?1")
	public Page<ColumnRelationArticlePO> findAllBySearch(String reg, Pageable page);

}
