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
import com.sumavision.tetris.cms.article.ArticlePO;
import com.sumavision.tetris.cms.article.ArticleRegionPermissionDAO;
import com.sumavision.tetris.cms.article.ArticleRegionPermissionPO;
import com.sumavision.tetris.cms.article.ArticleVO;
import com.sumavision.tetris.cms.classify.ClassifyVO;
import com.sumavision.tetris.cms.region.RegionVO;
import com.sumavision.tetris.cms.relation.ColumnRelationArticleDAO;
import com.sumavision.tetris.cms.relation.ColumnRelationArticlePO;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

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

	public ColumnPO addRoot() throws Exception {

		ColumnPO columnPO = new ColumnPO();
		columnPO.setName("新建的标签");
		columnPO.setUpdateTime(new Date());

		columnDao.save(columnPO);

		return columnPO;
	}

	public ColumnPO append(ColumnPO parent) throws Exception {

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

		columnDao.save(columnPO);

		return columnPO;
	}

	public ColumnPO update(ColumnPO columnPO, String name, String remark) throws Exception {

		columnPO.setName(name);
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

		List<ColumnPO> subCols = columnQuery.findAllSubTags(sourceCol.getId());

		if (subCols != null && subCols.size() > 0) {
			for (ColumnPO subCol : subCols) {
				String[] paths = subCol.getParentPath()
						.split(new StringBufferWrapper().append("/").append(sourceCol.getId()).toString());
				if (paths.length == 1) {
					subCol.setParentPath(parentPath.append("/").append(targetCol.getId()).toString());
				} else {
					subCol.setParentPath(parentPath.append("/").append(targetCol.getId()).append(paths[1]).toString());
				}
			}
		}

		if (subCols == null || subCols.size() <= 0)
			subCols = new ArrayList<ColumnPO>();
		subCols.add(sourceCol);

		columnDao.save(subCols);
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
	public void top(ColumnPO columnPO) throws Exception {

		if (columnPO.getParentId() == null)
			return;

		List<ColumnPO> subColumns = columnQuery.findAllSubTags(columnPO.getId());

		if (subColumns != null && subColumns.size() > 0) {

			String reg = new StringBufferWrapper().append("/").append(columnPO.getParentId()).toString();

			for (ColumnPO subCol : subColumns) {
				subCol.setParentPath(subCol.getParentPath().split(reg)[1]);
			}

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
	public ColumnVO query(Long columnId, Pageable page) throws Exception {
		
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
							break;
						}
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
	public ColumnVO queryCommand(Pageable page) throws Exception {
		
		ColumnVO view_column = new ColumnVO().setName("推荐")
											 .setArticles(new ArrayList<ArticleVO>());
		
		Page<ColumnRelationArticlePO> pages = columnRelationArticleDao.findByCommand(true, page);
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
								break;
							}
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
	 * 
	 * @param columnId 栏目id
	 * @param province 省
	 * @param city 市
	 * @param district 区
	 */
	public ColumnVO queryByRegion(Long columnId, String province, String city, String district, Pageable page) throws Exception {
		
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
		}else if(city != null && city != ""){
			String reg = new StringBufferWrapper().append("%")
												  .append(city)
												  .append("%")
												  .toString();
			regionArticleIds = articleRegionPermissionDao.findArticleIdByRegion(reg);			
		}else if(city == "" && district == ""){
			view_column = query(columnId, page);
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
								break;
							}
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
	 * 搜索文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午3:38:20
	 * @param search 搜索关键字
	 * @param page 分页
	 * @return
	 */
	public List<ArticleVO> search(String search, Pageable page) throws Exception{
		
		String reg = new StringBufferWrapper().append("%")
											  .append(search)
											  .append("%")
											  .toString();
		Page<ColumnRelationArticlePO> pages = columnRelationArticleDao.findAllBySearch(reg, page);
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
						view_articles.add(articleVO);
						break;
					}
				}

			}
		}
 		
 		return view_articles;
	}
}
