package com.sumavision.tetris.cms.article;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ArticleRegionPermissionPO.class, idClass = Long.class)
public interface ArticleRegionPermissionDAO extends BaseDAO<ArticleRegionPermissionPO>{

	/**
	 * 根据文章id列查询文章地区关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 下午3:16:25
	 * @param ids 文章id列表
	 * @return List<ArticleClassifyPermissionPO>
	 */
	public List<ArticleRegionPermissionPO> findByArticleIdIn(Collection<Long> ids);
	
	/**
	 * 根据文章id列表删除关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 上午11:01:24
	 * @param ids 文章id列表
	 */
	public void deleteByArticleIdIn(Collection<Long> ids);
}
