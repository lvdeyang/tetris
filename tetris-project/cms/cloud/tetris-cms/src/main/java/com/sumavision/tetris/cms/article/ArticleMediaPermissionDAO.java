package com.sumavision.tetris.cms.article;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ArticleMediaPermissionPO.class, idClass = Long.class)
public interface ArticleMediaPermissionDAO extends BaseDAO<ArticleMediaPermissionPO>{

	/**
	 * 查询文章关联的媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:03:54
	 * @param Long articleId 文章id
	 */
	public List<ArticleMediaPermissionPO> findByArticleId(Long articleId);
	
	/**
	 * 查询媒资关联的文章<br/>
	 * <b>作者:</b>Mr<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午9:20:54
	 * @param Long mediaId 媒资id
	 */
	public List<ArticleMediaPermissionPO> findByMediaId(Long mediaId);
	
}
