package com.sumavision.tetris.cms.relation;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

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

}
