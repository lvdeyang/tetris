package com.sumavision.tetris.cms.column;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.cms.article.ArticleClassifyPermissionDAO;
import com.sumavision.tetris.cms.article.ArticleClassifyPermissionPO;
import com.sumavision.tetris.cms.article.ArticleDAO;
import com.sumavision.tetris.cms.article.ArticleHistoryDAO;
import com.sumavision.tetris.cms.article.ArticleHistoryPO;
import com.sumavision.tetris.cms.article.ArticleKeepDAO;
import com.sumavision.tetris.cms.article.ArticleKeepPO;
import com.sumavision.tetris.cms.article.ArticlePO;
import com.sumavision.tetris.cms.article.ArticleRegionPermissionDAO;
import com.sumavision.tetris.cms.article.ArticleVO;
import com.sumavision.tetris.cms.classify.ClassifyVO;
import com.sumavision.tetris.cms.relation.ColumnRelationArticleDAO;
import com.sumavision.tetris.cms.relation.ColumnRelationArticlePO;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserVO;

/**
 * 内容模板增删改操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月18日 下午1:31:28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ColumnService {

	@Autowired
	private ColumnDAO columnDao;	

	@Autowired
	private ColumnQuery columnQuery;
	
	@Autowired
	private ColumnRelationArticleDAO columnRelationArticleDao;
	
	@Autowired
	private ArticleDAO articleDao;
	
	@Autowired
	private ArticleRegionPermissionDAO articleRegionPermissionDao;
	
	@Autowired
	private ArticleClassifyPermissionDAO articleClassifyPermissionDao;
	
	@Autowired
	private ColumnUserPermissionDAO columnUserPermissionDao;
	
	@Autowired
	private ArticleHistoryDAO articleHistoryDAO;
	
	@Autowired
	private ArticleKeepDAO articleKeepDAO;
	
	@Autowired
	private ColumnSubscriptionDAO columnSubscriptionDAO;

	public ColumnPO addRoot(UserVO user) throws Exception {

		ColumnPO columnPO = new ColumnPO();
		columnPO.setName("新建的标签");
		columnPO.setUpdateTime(new Date());
		
		List<ColumnVO> columnVOs = columnQuery.queryColumnRoot(user);
		
		if (columnVOs != null && columnVOs.size() > 0) {
			Collections.sort(columnVOs,new ColumnPO.ColumnVOOrderComparator());
			columnPO.setColumnOrder(columnVOs.get(columnVOs.size()-1).getColumnOrder() + 1);
		}else{
			columnPO.setColumnOrder(1l);
		}

		columnDao.save(columnPO);
		
		addPermission(user, columnPO);

		return columnPO;
	}

	public ColumnPO append(UserVO user, ColumnPO parent) throws Exception {

		StringBufferWrapper parentPath = new StringBufferWrapper();
		if (parent.getParentId() == null) {
			parentPath.append("/").append(parent.getId());
		} else {
			parentPath.append(parent.getParentPath()).append("/").append(parent.getId());
		}

		ColumnPO columnPO = new ColumnPO();
		columnPO.setName("新建的标签");
		columnPO.setParentId(parent.getId());
		columnPO.setParentPath(parentPath.toString());
		columnPO.setUpdateTime(new Date());
		
		Long setOrder = 1l;
		List<ColumnPO> columnVOs = columnDao.findByParentIdOrderByColumnOrder(parent.getId());
		if (columnVOs != null && columnVOs.size() > 0) {
			setOrder = columnVOs.get(columnVOs.size() - 1).getColumnOrder() + 1;
		}
		columnPO.setColumnOrder(setOrder);

		columnDao.save(columnPO);
		
		addPermission(user, columnPO);

		return columnPO;
	}

	public ColumnPO update(ColumnPO columnPO, String name, String code, String remark) throws Exception {

		columnPO.setName(name);
		columnPO.setCode(code);
		columnPO.setRemark(remark);
		columnPO.setUpdateTime(new Date());
		columnDao.save(columnPO);

		return columnPO;
	}

	public void remove(ColumnPO columnPO) throws Exception {

		List<ColumnPO> subColumnPOs = columnQuery.findAllSubTags(columnPO.getId());
		
		//删除关联
		List<ColumnRelationArticlePO> relationArticles = columnRelationArticleDao.findByColumnId(columnPO.getId());
		columnRelationArticleDao.deleteInBatch(relationArticles);
		columnUserPermissionDao.deleteByColumnId(columnPO.getId());
		
		Set<Long> colIds = new HashSetWrapper<Long>().add(columnPO.getId()).getSet();
		if (subColumnPOs != null && subColumnPOs.size() > 0) {
			for (ColumnPO column : subColumnPOs) {
				colIds.add(column.getId());
			}
		}

		if (subColumnPOs == null)
			subColumnPOs = new ArrayList<ColumnPO>();
		subColumnPOs.add(columnPO);
		columnDao.deleteInBatch(subColumnPOs);

	}

	public void move(ColumnPO sourceCol, ColumnPO targetCol) throws Exception {

		StringBufferWrapper parentPath = new StringBufferWrapper();
		if (targetCol.getParentId() == null) {
			parentPath.append("/").append(targetCol.getId());
		} else {
			parentPath.append(targetCol.getParentPath()).append("/").append(targetCol.getId());
		}

		sourceCol.setParentId(targetCol.getId());
		sourceCol.setParentPath(parentPath.toString());
		
		Long orderLong = 1l;
		List<ColumnPO> columnPOs = columnDao.findByParentIdOrderByColumnOrder(targetCol.getId());
		if (columnPOs != null && columnPOs.size() > 0) {
			orderLong = columnPOs.get(columnPOs.size() - 1).getColumnOrder() + 1;
		}
		sourceCol.setColumnOrder(orderLong);

		List<ColumnPO> subCols = columnQuery.findAllSubTags(sourceCol.getId());

		if (subCols != null && subCols.size() > 0) {
			for (ColumnPO subCol : subCols) {
				String[] paths = subCol.getParentPath()
						.split(new StringBufferWrapper().append("/").append(sourceCol.getId()).toString());
				if (paths.length == 1 || paths.length == 0) {
					subCol.setParentPath(new StringBufferWrapper().append(parentPath).append("/").append(sourceCol.getId()).toString());
				} else {
					subCol.setParentPath(new StringBufferWrapper().append(parentPath).append("/").append(sourceCol.getId()).append(paths[1]).toString());
				}
			}
		}

		if (subCols == null || subCols.size() <= 0)
			subCols = new ArrayList<ColumnPO>();
		subCols.add(sourceCol);

		columnDao.save(subCols);
	}
	
	/**
	 * 栏目排序上移<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 上午10:05:39
	 * @param columnPO 要上移的标签
	 */
	public List<ColumnVO> up(ColumnPO columnPO,UserVO user) throws Exception{
		List<ColumnPO> relations;
		
		if (columnPO.getParentId() == null) {
			relations = columnQuery.queryColumnRootPO(user);
		}else {
			relations = columnDao.findByParentIdOrderByColumnOrder(columnPO.getParentId());
		}
		
		if (relations != null && relations.size() > 0 && columnPO.getColumnOrder() != 1) {
			Long newOrder = null;
			Long oldOrder = null;
			
			if(relations != null && relations.size()>0){
				for(int i=0; i<relations.size(); i++){
					if(i != 0){
						ColumnPO relation = relations.get(i);
						ColumnPO upRelation = relations.get(i-1);
						if(relation.getUuid().equals(columnPO.getUuid())){
							newOrder = upRelation.getColumnOrder();
							oldOrder = relation.getColumnOrder();
							relation.setColumnOrder(newOrder);
							upRelation.setColumnOrder(oldOrder);
							columnDao.save(relations);
							break;
						}
					}
				}
			}
			
			Collections.sort(relations, new ColumnPO.ColumnOrderComparator());
		}
		
		return columnQuery.querycolumnTree(user);
	}

	/**
	 * 置顶一个标签<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月18日 上午10:05:39
	 * 
	 * @param TemplateTagPO
	 *            tag 待置顶的标签
	 */
	public void top(ColumnPO columnPO,UserVO user) throws Exception {

		if (columnPO.getParentId() == null)
			return;

		List<ColumnPO> subColumns = columnQuery.findAllSubTags(columnPO.getId());

		if (subColumns != null && subColumns.size() > 0) {

			String reg = new StringBufferWrapper().append("/").append(columnPO.getParentId()).toString();

			for (ColumnPO subCol : subColumns) {
				subCol.setParentPath(subCol.getParentPath().split(reg)[1]);
			}

		}
		
		List<ColumnVO> columnVOs = columnQuery.queryColumnRoot(user);
		Collections.sort(columnVOs,new ColumnPO.ColumnVOOrderComparator());
		if (columnVOs != null && columnVOs.size() > 0) {
			columnPO.setColumnOrder(columnVOs.get(columnVOs.size()-1).getColumnOrder() + 1);
		}else{
			columnPO.setColumnOrder(1l);
		}
		
		columnPO.setParentId(null);
		columnPO.setParentPath(null);

		if (subColumns == null)
			subColumns = new ArrayList<ColumnPO>();
		subColumns.add(columnPO);
		columnDao.save(subColumns);

	}
	
	/**
	 * 查询一个栏目下的内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月27日 上午10:05:39
	 * @param columnId 栏目id
	 */
	public ColumnVO query(UserVO user, Long columnId, Pageable page) throws Exception {
		
		ColumnPO column = columnDao.findOne(columnId);
		
		ColumnVO view_column = new ColumnVO().set(column);
		
		//栏目下子栏目
		List<ColumnPO> subColumns = columnQuery.findAllSubTags(columnId);		
		
		if(subColumns != null && subColumns.size()>0){
			List<ColumnVO> view_subColumns = ColumnVO.getConverter(ColumnVO.class).convert(subColumns, ColumnVO.class);			
			if(view_column.getSubColumns() == null) view_column.setSubColumns(new ArrayList<ColumnVO>());
			view_column.getSubColumns().addAll(view_subColumns);
		}

		//栏目下文章
		Page<ColumnRelationArticlePO> pages = columnRelationArticleDao.findByColumnId(columnId, page);
		List<ColumnRelationArticlePO> columnRelationArticles = pages.getContent();
		List<Long> articleIds = new ArrayList<Long>();
		for(ColumnRelationArticlePO columnRelationArticle: columnRelationArticles){
			articleIds.add(columnRelationArticle.getArticleId());
		}
		if(articleIds != null && articleIds.size()>0){
			List<ArticlePO> articles = articleDao.findAll(articleIds);
			List<ArticleClassifyPermissionPO> classifies = articleClassifyPermissionDao.findByArticleIdIn(articleIds);
			if(articles != null && articles.size()>0){
				List<ArticleVO> view_articles = new ArrayList<ArticleVO>();
				for(ArticlePO article: articles){
					ArticleVO view_article = new ArticleVO().set(article)
															.setClassifies(new ArrayList<ClassifyVO>());
					for(ArticleClassifyPermissionPO classify: classifies){
						if(classify.getArticleId().equals(article.getId())){
							ClassifyVO view_classify = new ClassifyVO();
							view_classify.setId(classify.getClassifyId())
										 .setName(classify.getClassifyName());
							view_article.getClassifies().add(view_classify);
						}
					}
					for(ColumnRelationArticlePO columnRelationArticle: columnRelationArticles){
						if(columnRelationArticle.getArticleId().equals(article.getId())){
							view_article.setOrder(columnRelationArticle.getArticleOrder());
							view_article.setColumnName(columnRelationArticle.getColumnName());
							view_article.setColumnId(columnRelationArticle.getColumnId());
							break;
						}
					}
					if(user.getId() != null && articleKeepDAO.findByUserIdAndArticleId(user.getId(), article.getId()) != null){
						view_article.setKeep(true);
					}
					view_articles.add(view_article);
				}
				
				Collections.sort(view_articles, new ColumnRelationArticlePO.ArticleVoOrderComparator());
				if(view_column.getArticles() == null) view_column.setArticles(new ArrayList<ArticleVO>());
				view_column.getArticles().addAll(view_articles);
			}
		}
		
		return view_column;
	}
	
	/**
	 * 查询推荐文章<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 下午4:03:16
	 */
	public ColumnVO queryCommand(UserVO user, Pageable page) throws Exception {
		
		ColumnVO view_column = new ColumnVO().setName("推荐")
											 .setArticles(new ArrayList<ArticleVO>());
		
		Page<ColumnRelationArticlePO> pages = null;
		if(user.getGroupId() != null){
			pages = columnRelationArticleDao.findCommandByGroupId(true, user.getGroupId(), page);
		}else if(user.getUuid() != null){
			pages = columnRelationArticleDao.findCommandByUserId(true, user.getUuid(), page);
		}
		List<ColumnRelationArticlePO> columnRelationArticles = pages.getContent();
		//推荐下文章
		List<Long> articleIds = new ArrayList<Long>();
		if(columnRelationArticles != null && columnRelationArticles.size() > 0){
			for(ColumnRelationArticlePO columnRelationArticle: columnRelationArticles){
				articleIds.add(columnRelationArticle.getArticleId());
			}
			
			if(articleIds != null && articleIds.size()>0){
				List<ArticlePO> articles = articleDao.findAll(articleIds);
				List<ArticleClassifyPermissionPO> classifies = articleClassifyPermissionDao.findByArticleIdIn(articleIds);
				if(articles != null && articles.size()>0){
					List<ArticleVO> view_articles = new ArrayList<ArticleVO>();
					for(ArticlePO article: articles){
						ArticleVO view_article = new ArticleVO().set(article)
																.setClassifies(new ArrayList<ClassifyVO>());
						for(ArticleClassifyPermissionPO classify: classifies){
							if(classify.getArticleId().equals(article.getId())){
								ClassifyVO view_classify = new ClassifyVO();
								view_classify.setId(classify.getClassifyId())
											 .setName(classify.getClassifyName());
								view_article.getClassifies().add(view_classify);
							}
						}
						for(ColumnRelationArticlePO columnRelationArticle: columnRelationArticles){
							if(columnRelationArticle.getArticleId().equals(article.getId())){
								view_article.setOrder(columnRelationArticle.getArticleOrder());
								view_article.setColumnName(columnRelationArticle.getColumnName());
								view_article.setColumnId(columnRelationArticle.getColumnId());
								break;
							}
						}
						if(user.getId() != null && articleKeepDAO.findByUserIdAndArticleId(user.getId(),article.getId()) != null){
							view_article.setKeep(true);
						}
						view_articles.add(view_article);
					}
					
					Collections.sort(view_articles, new ColumnRelationArticlePO.ArticleVoOrderComparator());
					if(view_column.getArticles() == null) view_column.setArticles(new ArrayList<ArticleVO>());
					view_column.getArticles().addAll(view_articles);
				}
			}			
		}
		
		return view_column;
	}
	
	/**
	 * 根据地区查询一个栏目下的内容<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月27日 上午10:05:39
	 * @param columnId 栏目id
	 * @param province 省
	 * @param city 市
	 * @param district 区
	 */
	public ColumnVO queryByRegion(UserVO user, Long columnId, String province, String city, String district, Pageable page) throws Exception {
		
		ColumnPO column = columnDao.findOne(columnId);
		
		ColumnVO view_column = new ColumnVO().set(column);
		
		//栏目下子栏目
		List<ColumnPO> subColumns = columnQuery.findAllSubTags(columnId);		
		
		if(subColumns != null && subColumns.size()>0){
			List<ColumnVO> view_subColumns = ColumnVO.getConverter(ColumnVO.class).convert(subColumns, ColumnVO.class);			
			if(view_column.getSubColumns() == null) view_column.setSubColumns(new ArrayList<ColumnVO>());
			view_column.getSubColumns().addAll(view_subColumns);
		}
		
		//栏目下文章		
		List<Long> regionArticleIds = new ArrayList<Long>();
		if(district != null && !district.equals("")){
			String reg = new StringBufferWrapper().append("%")
												  .append(district)
												  .append("%")
												  .toString();
			regionArticleIds = articleRegionPermissionDao.findArticleIdByRegion(reg);
			if (city != null && city != "") {
				String newReg = new StringBufferWrapper().append("%")
						  .append(city)
						  .append("%")
						  .toString();
				regionArticleIds.addAll(articleRegionPermissionDao.findArticleIdByRegion(newReg));
			}
		}else if(city != null && city != ""){
			String reg = new StringBufferWrapper().append("%")
												  .append(city)
												  .append("%")
												  .toString();
			regionArticleIds = articleRegionPermissionDao.findArticleIdByRegion(reg);			
		}else if(city == "" && district == ""){
			view_column = query(user, columnId, page);
		}
		
		if(regionArticleIds.size()>0){
			Page<Long> pages = columnRelationArticleDao.findArticleIdByColumnAndArticle(columnId, regionArticleIds, page);
			List<Long> articleIds = pages.getContent();
			 
			List<ColumnRelationArticlePO> columnRelationArticles = columnRelationArticleDao.findByColumnId(columnId);
			if(articleIds != null && articleIds.size()>0){
				List<ArticlePO> articles = articleDao.findAll(articleIds);
				List<ArticleClassifyPermissionPO> classifies = articleClassifyPermissionDao.findByArticleIdIn(articleIds);
				if(articles != null && articles.size()>0){
					List<ArticleVO> view_articles = new ArrayList<ArticleVO>();
					for(ArticlePO article: articles){
						ArticleVO view_article = new ArticleVO().set(article)
																.setClassifies(new ArrayList<ClassifyVO>());
						for(ArticleClassifyPermissionPO classify: classifies){
							if(classify.getArticleId().equals(article.getId())){
								ClassifyVO view_classify = new ClassifyVO();
								view_classify.setId(classify.getClassifyId())
											 .setName(classify.getClassifyName());
								view_article.getClassifies().add(view_classify);
							}
						}
						for(ColumnRelationArticlePO columnRelationArticle: columnRelationArticles){
							if(columnRelationArticle.getArticleId().equals(article.getId())){
								view_article.setOrder(columnRelationArticle.getArticleOrder());
								view_article.setColumnName(columnRelationArticle.getColumnName());
								view_article.setColumnId(columnRelationArticle.getColumnId());
								break;
							}
						}
						if(user.getId() != null && articleKeepDAO.findByUserIdAndArticleId(user.getId(), article.getId()) != null){
							view_article.setKeep(true);
						}
						view_articles.add(view_article);
					}
					
					Collections.sort(view_articles, new ColumnRelationArticlePO.ArticleVoOrderComparator());
					if(view_column.getArticles() == null) view_column.setArticles(new ArrayList<ArticleVO>());
					view_column.getArticles().addAll(view_articles);
				}
			}
		}
		
		return view_column;
	}
	
	/**
	 * 用户搜索文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午3:38:20
	 * @param user 用户
	 * @param search 搜索关键字
	 * @param page 分页
	 * @return
	 */
	public List<ArticleVO> search(UserVO user, String search, Pageable page) throws Exception{
		
		String reg = new StringBufferWrapper().append("%")
											  .append(search)
											  .append("%")
											  .toString();
		Page<ColumnRelationArticlePO> pages = null;
		if(user.getGroupId() != null){
			pages = columnRelationArticleDao.findAllWithGroupIdBySearch(reg, user.getGroupId(), page);
		}else if(user.getUuid() != null){
			pages = columnRelationArticleDao.findAllWithUserIdBySearch(reg, user.getUuid(), page);
		}
		long total = pages.getTotalElements();
		
		List<ArticleVO> view_articles = new ArrayList<ArticleVO>();
 		if(total > 0){
			List<ColumnRelationArticlePO> relations = pages.getContent();
			
			List<Long> articleIds = new ArrayList<Long>();
			for(ColumnRelationArticlePO relation: relations){
				articleIds.add(relation.getArticleId());
			}
			
			List<ArticlePO> articles = articleDao.findAll(articleIds);
			for(ColumnRelationArticlePO relation: relations){
				for(ArticlePO article: articles){			
					if(relation.getArticleId().equals(article.getId())){
						ArticleVO articleVO = new ArticleVO().set(article);	
						articleVO.setColumnName(relation.getColumnName());
						articleVO.setColumnId(relation.getColumnId());
						if(user.getId() != null && articleKeepDAO.findByUserIdAndArticleId(user.getId(), article.getId()) != null){
							articleVO.setKeep(true);
						}
						view_articles.add(articleVO);
						break;
					}
				}

			}
		}
 		
 		return view_articles;
	}
	
	/**
	 * 添加浏览历史<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param user 用户
	 * @param articleId 文章id
	 * @param columnId 栏目id
	 * @return
	 */
	public ArticleVO saveHistory(UserVO user, Long articleId,Long columnId) throws Exception{
		Boolean addBoolean = false;
		Boolean dealBoolean = false;
		
		List<ArticleHistoryPO> articleHistoryPOs = new ArrayList<ArticleHistoryPO>();
		if(user.getId() != null){
			articleHistoryPOs = articleHistoryDAO.findByUserId(user.getId());
		}
		
		if(articleHistoryPOs.size() > 0){
			Collections.sort(articleHistoryPOs, new ArticleHistoryPO.ArticleTimeComparator());
			for(ArticleHistoryPO item : articleHistoryPOs){
				if (item.getArticleId() == articleId) {
					item.setUpdateTime(new Date());
					item.setColumnId(columnId);
					articleHistoryDAO.save(item);
					dealBoolean = true;
					break;
				}
			}
		}
		
		if(!dealBoolean){
			ArticleHistoryPO article = new ArticleHistoryPO();
			article.setArticleId(articleId);
			article.setUserId(user.getId());
			article.setColumnId(columnId);
			article.setUpdateTime(new Date());
			articleHistoryDAO.save(article);
			addBoolean = true;
		}
		
		if(addBoolean && articleHistoryPOs.size() >= 20){
			articleHistoryDAO.delete(articleHistoryPOs.get(articleHistoryPOs.size() - 1).getId());
		}
		
		ArticleVO articleVO = new ArticleVO().set(articleDao.findOne(articleId));
		if(user.getId() != null && articleKeepDAO.findByUserIdAndArticleId(user.getId(), articleId) != null){
			articleVO.setKeep(true);
		}
 		
 		return articleVO;
	}
	
	/**
	 * 获取浏览历史<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param user 用户
	 * @return articleList 浏览历史文章列表
	 */
	public List<ArticleVO> getHistory(UserVO user) throws Exception{
		List<ArticleVO> articleList = new ArrayList<ArticleVO>();
		List<ArticleHistoryPO> articleHistoryPOs = new ArrayList<ArticleHistoryPO>();
		
		if(user.getId() != null){
			articleHistoryPOs = articleHistoryDAO.findByUserId(user.getId());
		}
		
		if(articleHistoryPOs.size() > 0){
			Collections.sort(articleHistoryPOs, new ArticleHistoryPO.ArticleTimeComparator());
			for(ArticleHistoryPO item:articleHistoryPOs){
				ArticlePO articlePO = articleDao.findOne(item.getArticleId());
				if (articlePO != null) {
					ArticleVO articleVO = new ArticleVO().set(articlePO);
					articleVO.setColumnId(item.getColumnId());
					articleVO.setColumnName(columnDao.findOne(item.getColumnId()).getName());
					articleVO.setWatchTime(item.getUpdateTime());
					if(user.getId() != null && articleKeepDAO.findByUserIdAndArticleId(user.getId(),item.getArticleId()) != null){
						articleVO.setKeep(true);
					}
					articleList.add(articleVO);
				}
			}
		}
 		
 		return articleList;
	}
	
	/**
	 * 删除指定浏览历史文章<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param user 用户
	 * @param articleId 文章id
	 * @param columnId 栏目id
	 * @return
	 */
	public List<ArticleVO> removeHistory(UserVO user,Long articleId) throws Exception{
		if(user.getId() != null){
			ArticleHistoryPO articleHistoryPO = articleHistoryDAO.findByUserIdAndArticleId(user.getId(), articleId);
			if (articleHistoryPO != null) {
				articleHistoryDAO.delete(articleHistoryPO);
			}
		}
		
		List<ArticleHistoryPO> articleHistoryPOs = articleHistoryDAO.findByUserId(user.getId());
		List<ArticleVO> articleVOs = new ArrayList<ArticleVO>();
		for (ArticleHistoryPO articleHistoryPO : articleHistoryPOs) {
			ArticlePO articlePO = articleDao.findOne(articleHistoryPO.getArticleId());
			if (articlePO != null) {
				ArticleVO articleVO = new ArticleVO().set(articlePO);
				if(user.getId() != null && articleKeepDAO.findByUserIdAndArticleId(user.getId(),articleHistoryPO.getArticleId()) != null){
					articleVO.setKeep(true);
				}
				articleVOs.add(articleVO);
			}
		}
		
		return articleVOs;
	}
	
	/**
	 * 清空浏览历史<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param user 用户
	 * @return
	 */
	public void clearHistory(UserVO user) throws Exception{
		if(user.getId() != null){
			List<ArticleHistoryPO> articleHistoryPOs = new ArrayList<ArticleHistoryPO>();
			articleHistoryPOs = articleHistoryDAO.findByUserId(user.getId());
			if(articleHistoryPOs.size() > 0){
				articleHistoryDAO.deleteInBatch(articleHistoryPOs);
			}
		}
	}
	
	/**
	 * 添加收藏文章<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param user 用户
	 * @param articleId 文章id
	 * @param columnId 栏目id
	 * @return
	 */
	public ArticleVO keep(UserVO user, Long articleId,Long columnId) throws Exception{
		Boolean addBoolean = false;
		Boolean dealBoolean = false;
		
		List<ArticleKeepPO> articleKeepPOs = new ArrayList<ArticleKeepPO>();
		if(user.getId() != null){
			articleKeepPOs = articleKeepDAO.findByUserId(user.getId());
		}
		
		if(articleKeepPOs.size() > 0){
			Collections.sort(articleKeepPOs, new ArticleKeepPO.ArticleTimeComparator());
			for(ArticleKeepPO item : articleKeepPOs){
				if (item.getArticleId() == articleId) {
					item.setUpdateTime(new Date());
					item.setColumnId(columnId);
					articleKeepDAO.save(item);
					dealBoolean = true;
					break;
				}
			}
		}
		
		if(!dealBoolean){
			ArticleKeepPO article = new ArticleKeepPO();
			article.setArticleId(articleId);
			article.setUserId(user.getId());
			article.setColumnId(columnId);
			article.setUpdateTime(new Date());
			articleKeepDAO.save(article);
			addBoolean = true;
		}
		
		if(addBoolean && articleKeepPOs.size() >= 20){
			articleKeepDAO.delete(articleKeepPOs.get(articleKeepPOs.size() - 1).getId());
		}
 		
 		return new ArticleVO().set(articleDao.findOne(articleId)).setKeep(true);
	}
	
	/**
	 * 获取收藏列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param user 用户
	 * @return articleList 收藏文章列表
	 */
	public List<ArticleVO> getKeep(UserVO user) throws Exception{
		List<ArticleVO> articleList = new ArrayList<ArticleVO>();
		List<ArticleKeepPO> articleKeepPOs = new ArrayList<ArticleKeepPO>();
		
		if(user.getId() != null){
			articleKeepPOs = articleKeepDAO.findByUserId(user.getId());
		}
		
		if(articleKeepPOs.size() > 0){
			Collections.sort(articleKeepPOs, new ArticleKeepPO.ArticleTimeComparator());
			for(ArticleKeepPO item:articleKeepPOs){
				ArticlePO articlePO = articleDao.findOne(item.getArticleId());
				if (articlePO != null) {
					ArticleVO articleVO = new ArticleVO().set(articlePO);
					articleVO.setColumnId(item.getColumnId());
					articleVO.setColumnName(columnDao.findOne(item.getColumnId()).getName());
					articleVO.setWatchTime(item.getUpdateTime());
					articleVO.setKeep(true);
					articleList.add(articleVO);
				}
			}
		}
 		
 		return articleList;
	}
	
	/**
	 * 删除指定收藏文章<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param user 用户
	 * @param articleId 文章id
	 * @param columnId 栏目id
	 * @return
	 */
	public List<ArticleVO> removeKeep(UserVO user,Long articleId) throws Exception{
		
		if(user.getId() != null){
			ArticleKeepPO articleKeepPO = articleKeepDAO.findByUserIdAndArticleId(user.getId(), articleId);
			if (articleKeepPO != null) {
				articleKeepDAO.delete(articleKeepPO);
			}
		}
		
		List<ArticleKeepPO> articleKeepPOs = articleKeepDAO.findByUserId(user.getId());
		List<ArticleVO> articleVOs = new ArrayList<ArticleVO>();
		for (ArticleKeepPO articleKeepPO : articleKeepPOs) {
			ArticlePO articlePO = articleDao.findOne(articleKeepPO.getArticleId());
			if (articlePO != null) {
				articleVOs.add(new ArticleVO().set(articlePO).setKeep(true));
			}
		}
		
		return articleVOs;
	}
	
	/**
	 * 清空收藏列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param user 用户
	 * @return
	 */
	public void clearKeep(UserVO user) throws Exception{
		if(user.getId() != null){
			List<ArticleKeepPO> articleKeepPOs = new ArrayList<ArticleKeepPO>();
			articleKeepPOs = articleKeepDAO.findByUserId(user.getId());
			if(articleKeepPOs.size() > 0){
				articleKeepDAO.deleteInBatch(articleKeepPOs);
			}
		}
	}
	
	/**
	 * 设置订阅栏目<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午11:18:58
	 * @param user 用户
	 * @param columnList 栏目id
	 * @return
	 */
	public void setSubscriptionColumnTree(UserVO userVO,List<Long> columnList){
		if (userVO.getId() != null) {
			List<ColumnSubscriptionPO> columnSubscriptionPOs = columnSubscriptionDAO.findByUserId(userVO.getId());
			if (columnSubscriptionPOs != null && columnSubscriptionPOs.size() > 0) {
				columnSubscriptionDAO.deleteInBatch(columnSubscriptionPOs);
			}
			if(columnList != null && columnList.size() > 0){
				for(Long column:columnList){
					ColumnSubscriptionPO columnSubscriptionPO = new ColumnSubscriptionPO();
					columnSubscriptionPO.setColumnId(column);
					columnSubscriptionPO.setUserId(userVO.getId());
					columnSubscriptionDAO.save(columnSubscriptionPO);
				}
			}
		}
	}
	
	/**
	 * 添加栏目用户关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月27日 下午1:28:14
	 * @param user 用户
	 * @param column 栏目
	 * @return ColumnUserPermissionPO 栏目用户关联
	 */
	public ColumnUserPermissionPO addPermission(UserVO user, ColumnPO column) throws Exception{
		
		ColumnUserPermissionPO permission = new ColumnUserPermissionPO();
		permission.setColumnId(column.getId());
		permission.setUserId(user.getUuid());
		permission.setGroupId(user.getGroupId());
		
		columnUserPermissionDao.save(permission);
		
		return permission;
	}
}
