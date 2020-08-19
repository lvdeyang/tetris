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
	 * 地区查询文章id<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午3:59:34
	 * @param Long columnId 栏目id
	 */
	@Query(value = "SELECT permission.articleId FROM com.sumavision.tetris.cms.article.ArticleRegionPermissionPO permission WHERE permission.regionName LIKE ?1")
	public List<Long> findArticleIdByRegion(String reg);
	
	/**
	 * 根据文章id列表删除关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 上午11:01:24
	 * @param ids 文章id列表
	 */
	public void deleteByArticleIdIn(Collection<Long> ids);
	
	/**
	 * 根据地区id删除关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 上午10:38:43
	 * @param regionId 地区id
	 */
	public void deleteByRegionId(Long regionId);
	
	/**
	 * 根据地区id列表删除关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 上午10:39:30
	 * @param regionIds 地区id列表
	 */
	public void deleteByRegionIdIn(Collection<Long> regionIds);
}
