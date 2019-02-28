package com.sumavision.tetris.cms.article;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ArticlePO.class, idClass = Long.class)
public interface ArticleDAO extends BaseDAO<ArticlePO>{

	/**
	 * 分页查询文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月24日 下午3:59:34
	 * @param Pageable page 分页信息
	 */
	public Page<ArticlePO> findAll(Pageable page);
	
	/**
	 * 分页查询文章列表（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午6:29:45
	 * @param Collection<Long> except 例外用户id列表
	 * @param Pageable page 分页信息
	 * @return Page<ArticlePO> 用户列表
	 */
	@Query(value = "SELECT * FROM TETRIS_CMS_ARTICLE article WHERE article.id NOT IN ?1")
	public Page<ArticlePO> findWithExcept(Collection<Long> except, Pageable page);
}
