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
	 * @param Collection<Long> except 例外文章id列表
	 * @param Pageable page 分页信息
	 * @return Page<ArticlePO> 文章列表
	 */
	@Query(value = "SELECT article FROM com.sumavision.tetris.cms.article.ArticlePO article WHERE article.id NOT IN ?1")
	public Page<ArticlePO> findWithExcept(Collection<Long> except, Pageable page);
	
	/**
	 * 根据用户id分页查询文章列表（带例外）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 下午7:07:47
	 * @param except  例外文章id列表
	 * @param userId 用户id
	 * @param page 分页
	 * @return Page<ArticlePO> 文章列表
	 */
	@Query(value = "SELECT article.* FROM TETRIS_CMS_ARTICLE article LEFT JOIN TETRIS_CMS_ARTICLE_USER_PERMISSION permission ON article.id = permission.article_id WHERE article.id NOT IN ?1 AND permission.user_id = ?2 \n#pageable\n",
			countQuery = "SELECT count(article.id) FROM TETRIS_CMS_ARTICLE article LEFT JOIN TETRIS_CMS_ARTICLE_USER_PERMISSION permission ON article.id = permission.article_id WHERE article.id NOT IN ?1 AND permission.user_id = ?2",
			nativeQuery = true)
	public Page<ArticlePO> findWithExceptByUserId(Collection<Long> except, String userId, Pageable page);
	
	/**
	 * 根据组织id分页查询文章列表（带例外）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 下午7:07:47
	 * @param except  例外文章id列表
	 * @param groupId 组织id
	 * @param page 分页
	 * @return Page<ArticlePO> 文章列表
	 */
	@Query(value = "SELECT article.* FROM TETRIS_CMS_ARTICLE article LEFT JOIN TETRIS_CMS_ARTICLE_USER_PERMISSION permission ON article.id = permission.article_id WHERE article.id NOT IN ?1 AND permission.group_id = ?2 \n#pageable\n",
			countQuery = "SELECT count(article.id) FROM TETRIS_CMS_ARTICLE article LEFT JOIN TETRIS_CMS_ARTICLE_USER_PERMISSION permission ON article.id = permission.article_id WHERE article.id NOT IN ?1 AND permission.group_id = ?2",
			nativeQuery = true)
	public Page<ArticlePO> findWithExceptByGroupId(Collection<Long> except, String groupId, Pageable page);
	
	/**
	 * 检索分页查询文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月23日 上午11:19:08
	 * @param name 文章民
	 * @param author 作者
	 * @param region 地区
	 * @param classify 分类
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @param page 分页信息
	 * @return  Page<ArticlePO> 文章列表
	 */
	@Query(value = "SELECT DISTINCT article.* " +
					"FROM TETRIS_CMS_ARTICLE article " +
					"LEFT JOIN TETRIS_CMS_ARTICLE_REGION_PERMISSION region ON article.id = region.article_id " +
					"LEFT JOIN TETRIS_CMS_ARTICLE_CLASSIFY_PERMISSION classify ON article.id = classify.article_id " +
					"WHERE article.name LIKE ?1 AND article.author LIKE ?2 AND IF(?3 = '%%', true, region.region_name LIKE ?3) AND IF(?4 = '%%', true, classify.classify_name LIKE ?4) " +
					"AND IF(?5 = '', true, article.publish_time >= ?5) AND IF(?6 = '', true, article.publish_time <= ?6) \n#pageable\n", 
					countQuery = "SELECT COUNT(DISTINCT article.id) " +
							"FROM TETRIS_CMS_ARTICLE article " +
							"LEFT JOIN TETRIS_CMS_ARTICLE_REGION_PERMISSION region ON article.id = region.article_id " +
							"LEFT JOIN TETRIS_CMS_ARTICLE_CLASSIFY_PERMISSION classify ON article.id = classify.article_id " +
							"WHERE article.name LIKE ?1 AND article.author LIKE ?2 AND IF(?3 = '%%', true, region.region_name LIKE ?3) AND IF(?4 = '%%', true, classify.classify_name LIKE ?4) " +
							"AND IF(?5 = '', true, article.publish_time >= ?5) AND IF(?6 = '', true, article.publish_time <= ?6)",
					nativeQuery = true)
	public Page<ArticlePO> findAllBySearch(String name, String author, String region, String classify, String beginTime, String endTime, Pageable pageable);

	/**
	 * 根据用户id检索分页查询文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月23日 上午11:19:08
	 * @param name 文章民
	 * @param author 作者
	 * @param region 地区
	 * @param classify 分类
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @param userId 用户id
	 * @param page 分页信息
	 * @return  Page<ArticlePO> 文章列表
	 */
	@Query(value = "SELECT DISTINCT article.* " +
					"FROM TETRIS_CMS_ARTICLE article " +
					"LEFT JOIN TETRIS_CMS_ARTICLE_REGION_PERMISSION region ON article.id = region.article_id " +
					"LEFT JOIN TETRIS_CMS_ARTICLE_CLASSIFY_PERMISSION classify ON article.id = classify.article_id " +
					"LEFT JOIN TETRIS_CMS_ARTICLE_USER_PERMISSION user ON article.id = user.article_id " +
					"WHERE article.name LIKE ?1 AND article.author LIKE ?2 AND IF(?3 = '%%', true, region.region_name LIKE ?3) AND IF(?4 = '%%', true, classify.classify_name LIKE ?4) " +
					"AND IF(?5 = '', true, article.publish_time >= ?5) AND IF(?6 = '', true, article.publish_time <= ?6) " +
					"AND user.user_id = ?7 \n#pageable\n", 
					countQuery = "SELECT COUNT(DISTINCT article.id) " +
							"FROM TETRIS_CMS_ARTICLE article " +
							"LEFT JOIN TETRIS_CMS_ARTICLE_REGION_PERMISSION region ON article.id = region.article_id " +
							"LEFT JOIN TETRIS_CMS_ARTICLE_CLASSIFY_PERMISSION classify ON article.id = classify.article_id " +
							"WHERE article.name LIKE ?1 AND article.author LIKE ?2 AND IF(?3 = '%%', true, region.region_name LIKE ?3) AND IF(?4 = '%%', true, classify.classify_name LIKE ?4) " +
							"AND IF(?5 = '', true, article.publish_time >= ?5) AND IF(?6 = '', true, article.publish_time <= ?6) " +
							"AND user.user_id = ?7",
					nativeQuery = true)
	public Page<ArticlePO> findAllWithUserIdBySearch(String name, String author, String region, String classify, String beginTime, String endTime, String userId, Pageable pageable);

	/**
	 * 根据组织id检索分页查询文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月23日 上午11:19:08
	 * @param name 文章名
	 * @param author 作者
	 * @param region 地区
	 * @param classify 分类
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @param groupId 用户id
	 * @param page 分页信息
	 * @return  Page<ArticlePO> 文章列表
	 */
	@Query(value = "SELECT DISTINCT article.* " +
					"FROM TETRIS_CMS_ARTICLE article " +
					"LEFT JOIN TETRIS_CMS_ARTICLE_REGION_PERMISSION region ON article.id = region.article_id " +
					"LEFT JOIN TETRIS_CMS_ARTICLE_CLASSIFY_PERMISSION classify ON article.id = classify.article_id " +
					"LEFT JOIN TETRIS_CMS_ARTICLE_USER_PERMISSION user ON article.id = user.article_id " +
					"WHERE article.name LIKE ?1 AND article.author LIKE ?2 AND IF(?3 = '%%', true, region.region_name LIKE ?3) AND IF(?4 = '%%', true, classify.classify_name LIKE ?4) " +
					"AND IF(?5 = '', true, article.publish_time >= ?5) AND IF(?6 = '', true, article.publish_time <= ?6) " +
					"AND user.group_id = ?7 \n#pageable\n", 
					countQuery = "SELECT COUNT(DISTINCT article.id) " +
							"FROM TETRIS_CMS_ARTICLE article " +
							"LEFT JOIN TETRIS_CMS_ARTICLE_REGION_PERMISSION region ON article.id = region.article_id " +
							"LEFT JOIN TETRIS_CMS_ARTICLE_CLASSIFY_PERMISSION classify ON article.id = classify.article_id " +
							"WHERE article.name LIKE ?1 AND article.author LIKE ?2 AND IF(?3 = '%%', true, region.region_name LIKE ?3) AND IF(?4 = '%%', true, classify.classify_name LIKE ?4) " +
							"AND IF(?5 = '', true, article.publish_time >= ?5) AND IF(?6 = '', true, article.publish_time <= ?6) " +
							"AND user.group_id = ?7",
					nativeQuery = true)
	public Page<ArticlePO> findAllWithGroupIdBySearch(String name, String author, String region, String classify, String beginTime, String endTime, String groupId, Pageable pageable);

	
	/**
	 * 根据用户id分页查询文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 下午5:07:47
	 * @param userId 用户id
	 * @param pageable 分页
	 * @return Page<ArticlePO 文章列表
	 */
	@Query(value = "SELECT article.* FROM TETRIS_CMS_ARTICLE article LEFT JOIN TETRIS_CMS_ARTICLE_USER_PERMISSION permission ON article.id = permission.article_id WHERE permission.user_id = ?1 \n#pageable\n",
			countQuery = "SELECT count(article.id) FROM TETRIS_CMS_ARTICLE article LEFT JOIN TETRIS_CMS_ARTICLE_USER_PERMISSION permission ON article.id = permission.article_id WHERE permission.user_id = ?1",
			nativeQuery = true)
	public Page<ArticlePO> findAllByUserId(String userId, Pageable pageable);
	
	/**
	 * 根据组织id分页查询文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 下午5:09:08
	 * @param groupId 组织id
	 * @param pageable 分页
	 * @return Page<ArticlePO> 文章列表
	 */
	@Query(value = "SELECT article.* FROM TETRIS_CMS_ARTICLE article LEFT JOIN TETRIS_CMS_ARTICLE_USER_PERMISSION permission ON article.id = permission.article_id WHERE permission.group_id = ?1 \n#pageable\n",
			countQuery = "SELECT count(article.id) FROM TETRIS_CMS_ARTICLE article LEFT JOIN TETRIS_CMS_ARTICLE_USER_PERMISSION permission ON article.id = permission.article_id WHERE permission.group_id = ?1",
			nativeQuery = true)
	public Page<ArticlePO> findAllByGroupId(String groupId, Pageable pageable);
}
