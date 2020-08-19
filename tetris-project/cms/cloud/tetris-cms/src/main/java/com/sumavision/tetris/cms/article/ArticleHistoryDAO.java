package com.sumavision.tetris.cms.article;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ArticleHistoryPO.class, idClass = Long.class)
public interface ArticleHistoryDAO extends BaseDAO<ArticleHistoryPO>{
	/**
	 * 根据用户组织id查询文章列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午3:11:32
	 * @param userId 用户id
	 * @return List<ArticleHistoryPO>
	 */
	public List<ArticleHistoryPO> findByUserId(Long userId);
	
	/**
	 * 根据用户组织id和文章id查询文章<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午3:11:32
	 * @param userId 用户id
	 * @param articleId 文章id
	 * @return List<ArticleKeepPO>
	 */
	public ArticleHistoryPO findByUserIdAndArticleId(Long userId,Long articleId);
}
