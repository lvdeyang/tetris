package com.sumavision.tetris.cms.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
}
