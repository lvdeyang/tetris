package com.sumavision.tetris.cms.article;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 文章查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月24日 下午3:58:50
 */
@Component
public class ArticleQuery {

	@Autowired
	private ArticleDAO articleDao;
	
	/**
	 * 分页查询文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月24日 下午4:11:33
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<ArticlePO> 文章列表
	 */
	public List<ArticlePO> findAll(int currentPage, int pageSize){
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ArticlePO> articles = articleDao.findAll(page);
		return articles.getContent();
	}
	
}
