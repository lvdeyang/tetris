package com.sumavision.tetris.cms.relation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cms.article.ArticleDAO;
import com.sumavision.tetris.cms.article.ArticlePO;
import com.sumavision.tetris.cms.article.exception.ArticleNotExistException;
import com.sumavision.tetris.cms.column.ColumnDAO;
import com.sumavision.tetris.cms.column.ColumnPO;
import com.sumavision.tetris.cms.column.exception.ColumnNotExistException;
import com.sumavision.tetris.lib.aliyun.push.AliPushService;

/**
 * 栏目关联文章增删改操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月26日 下午1:31:28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ColumnRelationArticleService {
	
	@Autowired
	private ColumnDAO columnDao;
	
	@Autowired
	private ArticleDAO articleDao;
	
	@Autowired
	private ColumnRelationArticleDAO columnRelationArticleDao;
	
	@Autowired
	private AliPushService aliPushService;

	/**
	 * 添加文章到栏目<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月26日 下午6:01:44
	 * @param Long columnId 栏目id
	 * @param List<String> articleIds 文章id列表
	 * @return List<ColumnRelationArticleVO> 栏目文章
	 */
	public List<ColumnRelationArticleVO> bindArticle(Long columnId, List<String> articleIds) throws Exception{
		
		ColumnPO column = columnDao.findById(columnId);
		if(column == null){
			throw new ColumnNotExistException(columnId);
		}
		
		Long tag = 0l;
		
		List<ColumnRelationArticlePO> exsitRelations = columnRelationArticleDao.findByColumnIdOrderByArticleOrderDesc(columnId);		
		if(exsitRelations != null && exsitRelations.size()>0){
			tag = exsitRelations.get(0).getArticleOrder();
		}
		
		if(articleIds!=null && articleIds.size()>0){
			Set<Long> transUserIds = new HashSet<Long>();
			for(String articleId:articleIds){
				transUserIds.add(Long.valueOf(articleId));
			}
			List<ArticlePO> articles = articleDao.findAllById(transUserIds);
			if(articles!=null && articles.size()>0){
				List<ColumnRelationArticlePO> relations = new ArrayList<ColumnRelationArticlePO>();
				for(ArticlePO article: articles){
					ColumnRelationArticlePO relation = new ColumnRelationArticlePO();
					relation.setUpdateTime(new Date());
					relation.setArticleId(article.getId());
					relation.setArticleName(article.getName());
					relation.setArticleRemark(article.getRemark());
					relation.setColumnId(column.getId());
					relation.setColumnName(column.getName());
					relation.setColumnCode(column.getCode());
					relation.setColumnRemark(column.getRemark());
					relation.setArticleOrder(++tag);
					relation.setCommand(false);
					relations.add(relation);
				}
				columnRelationArticleDao.saveAll(relations);
				
				List<ColumnRelationArticleVO> view_relations = ColumnRelationArticleVO.getConverter(ColumnRelationArticleVO.class).convert(relations, ColumnRelationArticleVO.class);
				
				return view_relations;
			}
		}
		return null;
	}
	
	/**
	 * 添加栏目到文章<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月19日 下午6:01:44
	 * @param Long articleId 文章id
	 * @param List<String> columnIds 栏目id列表
	 * @return List<ColumnRelationArticleVO> 栏目文章
	 */
	public List<ColumnRelationArticleVO> bindColumn(Long articleId, List<String> columnIds) throws Exception{
		
		ArticlePO article = articleDao.findById(articleId);
		if(article == null){
			throw new ArticleNotExistException(articleId);
		}
		
		Long tag = 0l;
		
		List<ColumnRelationArticlePO> exsitRelations = columnRelationArticleDao.findByArticleIdOrderByArticleOrderDesc(articleId);		
		if(exsitRelations != null && exsitRelations.size()>0){
			tag = exsitRelations.get(0).getArticleOrder();
		}
		
		//找需要添加的
		List<Long> needAddColumns = new ArrayList<Long>();
		for(String columnId: columnIds){
			boolean needAdd = true;
			for(ColumnRelationArticlePO exsitRelation: exsitRelations){
				if(exsitRelation.getColumnId().equals(Long.valueOf(columnId))){
					needAdd = false;
					break;
				}				
			}	
			if(needAdd){
				needAddColumns.add(Long.valueOf(columnId));
			}
		}
		
		//找需要删除的
		List<ColumnRelationArticlePO> needRemoveRelations = new ArrayList<ColumnRelationArticlePO>();
		for(ColumnRelationArticlePO exsitRelation: exsitRelations){
			boolean needRemove = true;
			for(String columnId: columnIds){
				if(Long.valueOf(columnId).equals(exsitRelation.getColumnId())){
					needRemove = false;
					break;
				}
			}
			if(needRemove){
				needRemoveRelations.add(exsitRelation);
			}
		}
		
		columnRelationArticleDao.deleteInBatch(needRemoveRelations);
		
		if(needAddColumns!=null && needAddColumns.size()>0){
			List<ColumnPO> columns = columnDao.findAllById(needAddColumns);
			if(columns!=null && columns.size()>0){
				List<ColumnRelationArticlePO> relations = new ArrayList<ColumnRelationArticlePO>();
				for(ColumnPO column: columns){
					ColumnRelationArticlePO relation = new ColumnRelationArticlePO();
					relation.setUpdateTime(new Date());
					relation.setArticleId(article.getId());
					relation.setArticleName(article.getName());
					relation.setArticleRemark(article.getRemark());
					relation.setColumnId(column.getId());
					relation.setColumnName(column.getName());
					relation.setColumnCode(column.getCode());
					relation.setColumnRemark(column.getRemark());
					relation.setArticleOrder(++tag);
					relation.setCommand(false);
					relations.add(relation);
				}
				columnRelationArticleDao.saveAll(relations);
				
				List<ColumnRelationArticleVO> view_relations = ColumnRelationArticleVO.getConverter(ColumnRelationArticleVO.class).convert(relations, ColumnRelationArticleVO.class);
				
				return view_relations;
			}
		}
		return null;
	}
	
	/**
	 * 栏目文章上移<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月27日 下午6:01:44
	 * @param Long columnId 栏目id
	 * @param Long articleId 文章id
	 * @return List<ColumnRelationArticleVO> 栏目文章
	 */
	public List<ColumnRelationArticleVO> up(Long columnId, Long articleId) throws Exception{
		
		List<ColumnRelationArticlePO> relations = columnRelationArticleDao.findByColumnIdOrderByArticleOrder(columnId);
		
		Long newOrder = null;
		Long oldOrder = null;
		
		if(relations != null && relations.size()>0){
			for(int i=0; i<relations.size(); i++){
				if(i != 0){
					ColumnRelationArticlePO relation = relations.get(i);
					ColumnRelationArticlePO upRelation = relations.get(i-1);
					if(relation.getColumnId().equals(columnId) && relation.getArticleId().equals(articleId)){
						newOrder = upRelation.getArticleOrder();
						oldOrder = relation.getArticleOrder();
						relation.setArticleOrder(newOrder);
						upRelation.setArticleOrder(oldOrder);
						columnRelationArticleDao.saveAll(relations);
						break;
					}
				}
			}
		}
		Collections.sort(relations, new ColumnRelationArticlePO.ArticleOrderComparator());
		return ColumnRelationArticleVO.getConverter(ColumnRelationArticleVO.class).convert(relations, ColumnRelationArticleVO.class);
	}
	
	/**
	 * 栏目文章下移<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月27日 下午6:01:44
	 * @param Long columnId 栏目id
	 * @param Long articleId 文章id
	 * @return List<ColumnRelationArticleVO> 栏目文章
	 */
	public List<ColumnRelationArticleVO> down(Long columnId, Long articleId) throws Exception{
		
		List<ColumnRelationArticlePO> relations = columnRelationArticleDao.findByColumnIdOrderByArticleOrder(columnId);
		
		Long newOrder = null;
		Long oldOrder = null;
		
		if(relations != null && relations.size()>0){
			for(int i=0; i<relations.size(); i++){
				if(i != relations.size()-1){
					ColumnRelationArticlePO relation = relations.get(i);
					ColumnRelationArticlePO downRelation = relations.get(i+1);
					if(relation.getColumnId().equals(columnId) && relation.getArticleId().equals(articleId)){
						newOrder = downRelation.getArticleOrder();
						oldOrder = relation.getArticleOrder();
						relation.setArticleOrder(newOrder);
						downRelation.setArticleOrder(oldOrder);
						columnRelationArticleDao.saveAll(relations);
						break;
					}
				}
			}
		}
		Collections.sort(relations, new ColumnRelationArticlePO.ArticleOrderComparator());
		return ColumnRelationArticleVO.getConverter(ColumnRelationArticleVO.class).convert(relations, ColumnRelationArticleVO.class);
	}
	
	/**
	 * 栏目文章置顶<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月27日 下午6:01:44
	 * @param Long columnId 栏目id
	 * @param Long articleId 文章id
	 * @return List<ColumnRelationArticleVO> 栏目文章
	 */
	public List<ColumnRelationArticleVO> top(Long columnId, Long articleId) throws Exception{
		
		List<ColumnRelationArticlePO> relations = columnRelationArticleDao.findByColumnIdOrderByArticleOrder(columnId);
		
		if(relations != null && relations.size()>0){
			for(int i=0; i<relations.size(); i++){
				ColumnRelationArticlePO relation = relations.get(i);
				if(relation.getColumnId().equals(columnId) && relation.getArticleId().equals(articleId)){
					if(i == 0) {
						break;
					}else{
						relation.setArticleOrder(1l);
						for(int j=0; j<relations.size(); j++){
							if(j < i){
								relations.get(j).setArticleOrder(relations.get(j).getArticleOrder()+1);
							}
						}
					}
					columnRelationArticleDao.saveAll(relations);
					break;
				}
			}
		}
		Collections.sort(relations, new ColumnRelationArticlePO.ArticleOrderComparator());
		return ColumnRelationArticleVO.getConverter(ColumnRelationArticleVO.class).convert(relations, ColumnRelationArticleVO.class);
	}
	
	/**
	 * 栏目文章置底<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月27日 下午6:01:44
	 * @param Long columnId 栏目id
	 * @param Long articleId 文章id
	 * @return List<ColumnRelationArticleVO> 栏目文章
	 */
	public List<ColumnRelationArticleVO> bottom(Long columnId, Long articleId) throws Exception{
		
		List<ColumnRelationArticlePO> relations = columnRelationArticleDao.findByColumnIdOrderByArticleOrder(columnId);
		
		if(relations != null && relations.size()>0){
			for(int i=0; i<relations.size(); i++){
				ColumnRelationArticlePO relation = relations.get(i);
				if(relation.getColumnId().equals(columnId) && relation.getArticleId().equals(articleId)){
					if(i == relations.size()-1) {
						break;
					}else{
						relation.setArticleOrder(relations.get(relations.size()-1).getArticleOrder());
						for(int j=0; j<relations.size(); j++){
							if(j > i){
								relations.get(j).setArticleOrder(relations.get(j).getArticleOrder()-1);
							}
						}
					}
					columnRelationArticleDao.saveAll(relations);
					break;
				}
			}
		}
		Collections.sort(relations, new ColumnRelationArticlePO.ArticleOrderComparator());
		return ColumnRelationArticleVO.getConverter(ColumnRelationArticleVO.class).convert(relations, ColumnRelationArticleVO.class);
	}
	
	/**
	 * 栏目文章通知<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月27日 下午6:01:44
	 * @param Long columnId 栏目id
	 * @param Long articleId 文章id
	 */
	public void inform(Long columnId, Long articleId) throws Exception{
		
		ArticlePO article = articleDao.findById(articleId);		
		
		JSONObject param = new JSONObject();
		param.put("url", article.getPreviewUrl());
 		
		aliPushService.sendMessage(article.getName(), article.getRemark(), param.toJSONString());		
		System.out.println("where is ios?");
		aliPushService.sendIosMessage(article.getName(), article.getRemark(), param.toJSONString());		

	}
}
