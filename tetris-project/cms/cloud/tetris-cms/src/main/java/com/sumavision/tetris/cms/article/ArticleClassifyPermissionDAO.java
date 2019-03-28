package com.sumavision.tetris.cms.article;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ArticleClassifyPermissionPO.class, idClass = Long.class)
public interface ArticleClassifyPermissionDAO extends BaseDAO<ArticleClassifyPermissionPO>{

	/**
	 * 根据文章id列表查询文章分类关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 下午3:11:32
	 * @param ids 文章id列表
	 * @return List<ArticleClassifyPermissionPO>
	 */
	public List<ArticleClassifyPermissionPO> findByArticleIdIn(List<Long> ids);
	
	/**
	 * 根据文章列表删除关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 上午10:59:13
	 * @param ids 文章ids
	 */
	public void deleteByArticleIdIn(Collection<Long> ids);
	
	/**
	 * 根据分类id删除关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 上午10:41:09
	 * @param classifyId 分类id
	 */
	public void deleteByClassifyId(Long classifyId);
	
	/**
	 * 根据分类id列表删除关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 上午10:41:47
	 * @param classifyIds 分类id列表
	 */
	public void deleteByClassifyIdIn(Collection<Long> classifyIds);
}